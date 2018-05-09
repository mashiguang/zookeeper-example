package cn.niceabc.zk.conn;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Znode2Del implements Watcher {

    private static Logger log = LoggerFactory.getLogger(Znode2Del.class);

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        ZooKeeper zk = new ZooKeeper("192.168.199.211:2181",
                500,
                new Znode2Del());
        log.debug("zk state: {}", zk.getState());

        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("zk session established.");

        String path3 = zk.create("/zk-test-persistent",
                "this is value".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        log.debug("success create znode {}", path3);


        Stat stat = zk.exists("/zk-test-persistent", null);
        if (stat != null) {

            zk.delete("/zk-test-persistent",
                    stat.getVersion(),
                    new AsyncCallback.VoidCallback() {
                        public void processResult(int i, String s, Object o) {
                            log.debug("i: {}", i);
                            log.debug("s: {}", s);
                            log.debug("o: {}", o);
                            log.debug("removed.");
                        }
                    },
                    "this is a context.");

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
