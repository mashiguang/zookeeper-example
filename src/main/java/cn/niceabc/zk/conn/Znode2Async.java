package cn.niceabc.zk.conn;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Znode2Async implements Watcher {

    private static Logger log = LoggerFactory.getLogger(Znode2Async.class);

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        ZooKeeper zk = new ZooKeeper("192.168.199.211:2181",
                500,
                new Znode2Async());
        log.debug("zk state: {}", zk.getState());

        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("zk session established.");

        zk.create("/zk-test-ephemeral",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL,
                new AsyncCallback.StringCallback() {
                    public void processResult(int i, String s, Object o, String s1) {
                        log.debug("znode created. {}", s);
                        log.debug("i: {}", i);
                        log.debug("s: {}", s);
                        log.debug("o: {}", o);
                        log.debug("s1: {}", s1);
                    }
                },
                "this is a context."
        );
        //log.debug("success create znode {}", path1);

        System.in.read();
    }

    public void process(WatchedEvent watchedEvent) {
        log.debug("receive watched event: {}", watchedEvent);

        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
