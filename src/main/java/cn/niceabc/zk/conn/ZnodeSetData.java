package cn.niceabc.zk.conn;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZnodeSetData implements Watcher {

    private static Logger log = LoggerFactory.getLogger(ZnodeSetData.class);

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        ZooKeeper zk = new ZooKeeper("192.168.199.211:2181",
                500,
                new ZnodeSetData());
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

        zk.setData(path, "456".getBytes(), stat.getVersion());
        String value2 = new String(zk.getData(path, false, stat));
        log.debug("value2: {}", value2);
        log.debug("stat.version: {}", stat.getVersion());

        stat = zk.setData(path, "789".getBytes(), -1);
        log.debug("stat.version: {}", stat.getVersion());

        // illegal stat.version
        try {
            zk.setData(path, "789".getBytes(), 99);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.in.read();
    }

    public void process(WatchedEvent watchedEvent) {
        log.debug("receive watched event: {}", watchedEvent);

        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
