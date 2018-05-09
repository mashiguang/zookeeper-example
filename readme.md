# zookeeper-example

install zookeeper

    docker run --name zk --restart always -d -p 0.0.0.0:2181:2181 -p 0.0.0.0:2888:2888 -p 0.0.0.0:3888:3888 zookeeper

Conn2，复用sessionid, sessonPasswd的意义是什么？

del未成功，cn.niceabc.zk.conn.Znode2Del
返回101代表什么?

书上说-1是万能version，但测试过程中，用-1还是报了BadVersionException，
cn.niceabc.zk.conn.ZnodeSetData