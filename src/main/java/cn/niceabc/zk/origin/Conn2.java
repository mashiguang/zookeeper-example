package cn.niceabc.zk.origin;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Conn2 implements Watcher {

    private static Logger log = LoggerFactory.getLogger(Conn2.class);

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {

        ZooKeeper zk = new ZooKeeper("192.168.199.211:2181",
                5000,
                new Conn2());
        log.debug("zk state: {}", zk.getState());

        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.debug("zk session established.");
        long sessionid = zk.getSessionId();
        byte[] pwd = zk.getSessionPasswd();

        zk = new ZooKeeper("192.168.199.211:2181",
                5000,
                new Conn2(),
                1L,             //illegal sessionid
                "test".getBytes());       //illegal pwd
        log.debug("zk state: {}", zk.getState());

        zk = new ZooKeeper("192.168.199.211:2181",
                5000,
                new Conn2(),
                sessionid,             //correct sessionid
                pwd);                  //correct pwd
        log.debug("zk state: {}", zk.getState());

        //System.in.read();
        TimeUnit.SECONDS.sleep(60);
    }

    public void process(WatchedEvent watchedEvent) {
        log.debug("receive watched event: {}", watchedEvent);

        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
