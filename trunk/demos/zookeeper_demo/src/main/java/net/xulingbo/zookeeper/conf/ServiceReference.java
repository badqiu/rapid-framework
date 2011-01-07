package net.xulingbo.zookeeper.conf;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xulingbo.zookeeper.ZooKeeperClient;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ServiceReference {
    
    static Map<String,List<String>> serviceIps = new HashMap();
    
    public static void reference(String serviceType,String namespace) throws KeeperException, InterruptedException {
        final String servicePath = "/services/"+namespace+"/soap";
        
        final ZooKeeper zooKeeper = ZooKeeperClient.zooKeeper;
        while(true)  {
            final Watcher watcher = new Watcher() {
                public void process(WatchedEvent event) {
                    System.out.println("ServiceReference process() event:"+event.getType()+" state:"+event.getState());
                    if(event.getType().equals(EventType.NodeChildrenChanged)) {
                        List<String> ips;
                        try {
                            ips = zooKeeper.getChildren(servicePath, true);
                            setServiceReference(servicePath,ips);
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            
            List<String> childs = zooKeeper.getChildren(servicePath, watcher);
            if(childs != null) {
                List<String> ips = zooKeeper.getChildren(servicePath, true);
                setServiceReference(servicePath,ips);
            }
            
            synchronized (ZooKeeperClient.mutex) {
                ZooKeeperClient.mutex.wait();
            }
        }
    }
    
    public static void setServiceReference(String namespace,List<String> ips) {
        System.out.println("setServiceReference:"+namespace+" ips:"+ips);
        serviceIps.put(namespace, ips);
    }
    
    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        ZooKeeperClient client = new ZooKeeperClient();
        client.connect("localhost:2181");
        
        reference("soap", "com.company.service.BlogService");
        
        Thread.sleep(100000 * 100);
    }
}
