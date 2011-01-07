package net.xulingbo.zookeeper.locks;

import java.io.IOException;

import junit.framework.TestCase;
import net.xulingbo.zookeeper.ZooKeeperClient;


public class LockTest extends TestCase {

    public void test() throws IOException, InterruptedException {
        ZooKeeperClient client = new ZooKeeperClient();
        client.connect("localhost:2181");
        
        final DistributedLock distributedLock = DistributedLockFactory.newLock("blog", "1");
        
        long cost = MultiThreadTestUtils.executeAndWait(30, new Runnable() {
            public void run() {
                try {
                    distributedLock.lock();
                    System.out.println("threadName:"+Thread.currentThread().getName());
                }finally {
                    distributedLock.unlock();
                }
            }
            
        });
        System.out.println("totalCost:"+cost);
    }
    
    public void test_with_newlock() throws IOException, InterruptedException {
        ZooKeeperClient client = new ZooKeeperClient();
        client.connect("localhost:2181");
        
        
        long cost = MultiThreadTestUtils.executeAndWait(30, new Runnable() {
            public void run() {
                final DistributedLock distributedLock = DistributedLockFactory.newLock("blog", "1");
                try {
                    distributedLock.lock();
                    System.out.println("threadName:"+Thread.currentThread().getName());
                }finally {
                    distributedLock.unlock();
                }
            }
            
        });
        System.out.println("totalCost:"+cost);
    }
}
