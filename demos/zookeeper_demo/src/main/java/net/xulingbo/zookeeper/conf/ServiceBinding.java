package net.xulingbo.zookeeper.conf;

import java.io.IOException;
import java.util.ArrayList;

import net.xulingbo.zookeeper.ZooKeeperClient;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;

public class ServiceBinding {
    
    public static void binding(String serviceType,String namespace,String clientID) throws KeeperException, InterruptedException {
        String servicePath = "/services/"+namespace+"/soap";
        
        ZooKeeper zooKeeper = ZooKeeperClient.zooKeeper;
        createIfNotExists(zooKeeper,"/services",null,Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT );
        createIfNotExists(zooKeeper,"/services/"+namespace,null,Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT );
        createIfNotExists(zooKeeper,servicePath,null,Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT );
        
        createIfNotExists(zooKeeper,servicePath+"/"+clientID, ("soap:version100"), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        
    }

    public static void createIfNotExists(ZooKeeper zooKeeper, String path,String data, ArrayList<ACL> arrayList,CreateMode createMode )
                                                                                  throws KeeperException,
                                                                                  InterruptedException {
        if(zooKeeper.exists(path, false) == null) {
            zooKeeper.create(path,data == null ? null : data.getBytes(), arrayList, createMode);
        }
    }
 
    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        ZooKeeperClient client = new ZooKeeperClient();
        client.connect("localhost:2181");
        
        binding("soap", "com.company.service.BlogService", "192.168.1.1");
        
        System.out.println("bindinged");
        Thread.sleep(1000000);
    }
}
