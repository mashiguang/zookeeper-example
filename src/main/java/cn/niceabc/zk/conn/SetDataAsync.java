package cn.niceabc.zk.conn;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class SetDataAsync implements Watcher {

    private static Logger log = LoggerFactory.getLogger(SetDataAsync.class);

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        ZooKeeper zk = new ZooKeeper("192.168.199.211:2181",
                500,
                new SetDataAsync());
        log.debug("zk state: {}", zk.getState());

        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("zk session established.");

        String path = "/zk-test-ephemeral";
        Stat stat = zk.exists(path, false);
        if (stat != null)
            zk.delete(path, stat.getVersion());
        else
            stat = new Stat();

        String path3 = zk.create(path,
                "this is value".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        log.debug("success create znode {}", path3);

        String value1 = new String(zk.getData(path, false, stat));
        log.debug("value1: {}", value1);
        log.debug("stat.version: {}", stat.getVersion());

        zk.setData(path, "789".getBytes(), stat.getVersion(), new AsyncCallback.StatCallback() {
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                log.debug("rc: {}", rc);
                log.debug("path: {}", path);
                log.debug("ctx: {}", ctx);
                log.debug("stat: {}", stat);
            }
        },"this is the context.");


        System.in.read();
    }

    public void process(WatchedEvent watchedEvent) {
        log.debug("receive watched event: {}", watchedEvent);

        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
