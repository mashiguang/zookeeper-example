package cn.niceabc.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Conn {

    private static Logger log = LoggerFactory.getLogger(Conn.class);

    public static void main(String[] args) throws InterruptedException {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);

        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString("192.168.199.211:2181")
                .sessionTimeoutMs(1000)
                .connectionTimeoutMs(1000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();

        client.blockUntilConnected();
        log.debug("started?");

        TimeUnit.SECONDS.sleep(10);
    }
}
