package cn.niceabc.zk.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Conn {
    private static Logger log = LoggerFactory.getLogger(Conn.class);

    public static void main(String[] args) {
        ZkClient zk = new ZkClient("192.168.199.211:2181", 5000);
        log.debug("zk session established.");

        zk.createPersistent("/zk-book/c1", true);
        zk.createPersistent("/zk-book/c2",true);


        zk.getChildren("/zk-book")
                .forEach(c -> log.debug(c));

        zk.deleteRecursive("/zk-book");

        log.debug("/zk-book exists? {}", zk.exists("/zk-book"));

    }
}
