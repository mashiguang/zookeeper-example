# zookeeper-example

install zookeeper

    docker run --name zk --restart always -d -p 0.0.0.0:2181:2181 -p 0.0.0.0:2888:2888 -p 0.0.0.0:3888:3888 zookeeper

Conn2，复用sessionid, sessonPasswd的意义是什么？

del未成功，cn.niceabc.zk.origin.Znode2Del
返回101代表什么?

Ephemeral, Persistent这两种节点生命周期有什么不同？

Ephemeral节点不能有子节点？
zk中规定所有非叶子节点必须是持久节点，即临时节点只能用在叶子节点。