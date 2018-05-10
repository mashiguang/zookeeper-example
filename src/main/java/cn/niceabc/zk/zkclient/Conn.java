package cn.niceabc.zk.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Conn {
    private static Logger log = LoggerFactory.getLogger(Conn.class);

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.199.211:2181", 5000);
        log.debug("zk session established.");

        zkClient.createPersistent("/zk-book/c1", true);
        zkClient.createPersistent("/zk-book/c2",true);

        List<String> children = zkClient.getChildren("/zk-book");
        children.forEach(c -> log.debug(c));
    }
}
