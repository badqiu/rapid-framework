package net.xulingbo.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperClient implements Watcher {
    public static ZooKeeper zooKeeper = null;
    public static int sessionTimeout = 20000;
    public static Object mutex = new Object();
    private List<Watcher> watchers = new ArrayList<Watcher>();
    
    public void connect(String connectString) throws IOException {
        zooKeeper = new ZooKeeper("localhost:2181",sessionTimeout,this);
        System.out.println("connect state:"+zooKeeper.getState());
    }
    
    public void process(WatchedEvent event) {
        synchronized (mutex) {
            mutex.notify();
        }
        for(Watcher w : watchers) {
            w.process(event);
        }
        System.out.println("processed():"+event.getType()+" path:"+event.getPath());
    }
    
}
