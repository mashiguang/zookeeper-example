package cn.niceabc.zk.origin;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZnodeWatch {

    private static Logger log = LoggerFactory.getLogger(ZnodeWatch.class);

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        ZooKeeper zk = new ZooKeeper("192.168.199.211:2181",
                500,
                new Watcher() {
                    public void process(WatchedEvent event) {
                        log.debug("receive watched event: {}", event);

                        if (Event.KeeperState.SyncConnected == event.getState()) {
                            connectedSemaphore.countDown();
                        }
                    }
                });
        log.debug("zk state: {}", zk.getState());

        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("zk session established.");

        Stat stat = zk.exists("/zk-test-persistent", new Watcher() {
            public void process(WatchedEvent event) {
                log.debug("event.state {}", event.getType());
            }
        });
        if (stat != null)
            zk.delete("/zk-test-persistent", stat.getVersion());

        String path3 = zk.create("/zk-test-persistent",
                "this is value".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        log.debug("success create znode {}", path3);


        System.in.read();
    }

}
