package cn.niceabc.zk.conn;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Conn1 implements Watcher {

    private static Logger log = LoggerFactory.getLogger(Conn1.class);

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {

        ZooKeeper zk = new ZooKeeper("192.168.199.211:2181",
                500,
                new Conn1());
        log.debug("zk state: {}", zk.getState());

        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("zk session established.");
    }

    public void process(WatchedEvent watchedEvent) {
        log.debug("receive watched event: {}", watchedEvent);

        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
