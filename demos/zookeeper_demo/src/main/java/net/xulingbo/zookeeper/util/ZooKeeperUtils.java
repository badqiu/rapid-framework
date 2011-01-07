package net.xulingbo.zookeeper.util;

import java.util.ArrayList;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperUtils {
    public static String createIfNotExists(ZooKeeper zooKeeper, String path, String data,
                                         ArrayList<ACL> arrayList, CreateMode createMode)
                                                                                         throws KeeperException,
                                                                                         InterruptedException {
        return createIfNotExists(zooKeeper,path,data.getBytes(),arrayList,createMode);
    }
    
    public static synchronized String createIfNotExists(ZooKeeper zooKeeper, String path, byte[] data,
                                         ArrayList<ACL> arrayList, CreateMode createMode)
                                                                                         throws KeeperException,
                                                                                         InterruptedException {
        Stat exists = zooKeeper.exists(path, false);
        if (exists == null) {
            String result =  zooKeeper.create(path, data == null ? null : data, arrayList, createMode);
            return result;
        }else {
            Object result = zooKeeper.getChildren(path, false, exists);
            return path;
        }
    }
}
