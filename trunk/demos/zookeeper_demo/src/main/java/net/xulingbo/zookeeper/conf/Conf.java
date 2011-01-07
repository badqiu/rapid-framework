package net.xulingbo.zookeeper.conf;

import java.io.IOException;

import net.xulingbo.zookeeper.ZooKeeperClient;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

public class Conf {
    
    /**
     * @param args
     * @throws IOException 
     * @throws InterruptedException 
     * @throws KeeperException 
     */
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        new Conf().execute();
    }

    String clientId = getClass().getSimpleName();
    private void execute() throws IOException, KeeperException, InterruptedException {
        
        ZooKeeperClient client = new ZooKeeperClient();
        client.connect("localhost:2181");
        
//        ZooKeeperClient.zooKeeper.create("/testRootPath", "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT); 
//        ZooKeeperClient.zooKeeper.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        
//        ZooKeeperClient.zooKeeper.create("/services", "".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        ZooKeeperClient.zooKeeper.create("/services/com.company.project.HelloService", "234234".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        
        System.out.println(new String(ZooKeeperClient.zooKeeper.getData("/services/com.company.project.HelloService",false,null)));
    }


}
