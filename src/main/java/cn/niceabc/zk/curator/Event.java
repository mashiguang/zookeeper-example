package cn.niceabc.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Event {

    private static Logger log = LoggerFactory.getLogger(Event.class);

    public static void main(String[] args) throws Exception {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);

        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString("192.168.199.230:2181")
                .sessionTimeoutMs(1000)
                .connectionTimeoutMs(1000)
                .retryPolicy(retryPolicy)
                .namespace("space1")
                .build();
        client.start();

        client.blockUntilConnected();
        log.debug("started?");

        PathChildrenCache cache = new PathChildrenCache(client, "/path1", true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                log.debug("event: {}", event);
            }
        });

        client
                .create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath("/path1/c1", "init".getBytes());

        Stat stat = new Stat();
        byte[] value = client.getData()
                .storingStatIn(stat)
                .forPath("/path1/c1");
        log.debug("value: {}", new String(value));

        client.delete()
                .deletingChildrenIfNeeded()
                .withVersion(stat.getVersion())
                .forPath("/path1/c1");

        TimeUnit.SECONDS.sleep(10);
    }
}
