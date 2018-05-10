# zookeeper-example

install zookeeper

    docker run --name zk --restart always -d -p 0.0.0.0:2181:2181 -p 0.0.0.0:2888:2888 -p 0.0.0.0:3888:3888 zookeeper

Conn2，复用sessionid, sessonPasswd的意义是什么？

del未成功，cn.niceabc.zk.origin.Znode2Del
返回101代表什么?

Ephemeral, Persistent这两种节点生命周期有什么不同？