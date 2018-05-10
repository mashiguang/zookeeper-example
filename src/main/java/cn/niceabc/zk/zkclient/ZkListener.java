package cn.niceabc.zk.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ZkListener {
    private static Logger log = LoggerFactory.getLogger(ZkListener.class);

    public static void main(String[] args) {
        ZkClient zk = new ZkClient("192.168.199.211:2181", 5000);
        log.debug("zk session established.");

        // async
        zk.subscribeChildChanges("/zk-book", new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                log.debug("s: {}", s);
                log.debug("children: {}", list);
            }
        });

        log.debug("create /zk-book/c1");
        zk.createPersistent("/zk-book/c1", true);

        log.debug("create /zk-book/c2");
        zk.createPersistent("/zk-book/c2",true);

        log.debug("children: {}", zk.getChildren("/zk-book"));

        log.debug("delete /zk-book/c1");
        zk.delete("/zk-book/c1");

        log.debug("delete /zk-book/c2");
        zk.delete("/zk-book/c2");

        log.debug("/zk-book exists? {}", zk.exists("/zk-book"));

    }
}
