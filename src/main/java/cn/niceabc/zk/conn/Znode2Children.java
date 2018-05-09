package cn.niceabc.zk.conn;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Znode2Children implements Watcher {

    private static Logger log = LoggerFactory.getLogger(Znode2Children.class);

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        ZooKeeper zk = new ZooKeeper("192.168.199.211:2181",
                500,
                new Znode2Children());
        log.debug("zk state: {}", zk.getState());

        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("zk session established.");

        List<String> children = zk.getChildren("/", null);
        log.debug("children:");
        for (String c: children) {
            log.debug(c);
        }

        // System.in.read();
    }

    public void process(WatchedEvent watchedEvent) {
        log.debug("receive watched event: {}", watchedEvent);

        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
