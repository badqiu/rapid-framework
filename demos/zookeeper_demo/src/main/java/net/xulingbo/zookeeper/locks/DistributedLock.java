package net.xulingbo.zookeeper.locks;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.xulingbo.zookeeper.util.ZooKeeperUtils;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
/**
 * 分布式锁
 * @author zhongxuan
 * @version $Id: DistributedLock.java,v 0.1 2011-1-7 下午02:54:30 zhongxuan Exp $
 */
public class DistributedLock implements Lock{
    public static final Logger log = Logger.getLogger(Locks.class);
    
    private String lockGroup;
    private String lockId;
    
    ////本机器的多线程锁
    private final Lock localhostLock = new ReentrantLock(); 
    
    private ZooKeeper zooKeeper = null;
//    private String realNode = null;
    boolean lockGroupCreateSuccess = false;
    
    private String lockPath;
    public DistributedLock(ZooKeeper zooKeeper,String lockGroup, String lockId) {
        super();
        this.lockGroup = lockGroup;
        this.lockId = lockId;
        this.zooKeeper = zooKeeper;
    }

    private synchronized void createLockGroupPath(boolean ignoreException) {
        try {
            if(lockGroupCreateSuccess) {
//                return null;
            }
            ZooKeeperUtils.createIfNotExists(zooKeeper,"/lock", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            ZooKeeperUtils.createIfNotExists(zooKeeper,getLockGroupPath(), new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            ZooKeeperUtils.createIfNotExists(zooKeeper,getLookIdPath(), new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            lockGroupCreateSuccess = true;
//            return realNode;
        } catch (KeeperException.NodeExistsException e) {
            log.error(e);
        } catch (KeeperException e) {
            log.error(e);
            // throw new
            if(ignoreException) {
//                return null;
            }else {
                throw new RuntimeException("cannot create lockGroupPath:"+getLockGroupPath(),e);
            }
        } catch (InterruptedException e) {
            log.error(e);
            if(ignoreException) {
//                return null;
            }else {
                throw new RuntimeException("cannot create lockGroupPath:"+getLockGroupPath(),e);
            }
        }
    }

    private String getLookIdPath() {
        return getLockGroupPath()+"/"+lockId;
    }

    private String getLockGroupPath() {
        return "/lock/"+lockGroup;
    }
    
    public void lock() {
        
        long start = System.currentTimeMillis();
        
        try {
            localhostLock.lock();
            createLockGroupPath(false);
            String realLockPath = zooKeeper.create(getLookIdPath()+"/lock" , new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
            
            while(true) {
                List<String> list = zooKeeper.getChildren(getLookIdPath(),new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        synchronized (DistributedLock.this) {
                            DistributedLock.this.notify();
                        }
                    }
                });
                Collections.sort(list);
                if(realLockPath.equals(getLookIdPath()+"/"+list.get(0))){
                    if(lockPath != null) throw new IllegalArgumentException("lockPath must be null");
                    lockPath = realLockPath;
                    break;
                }else {
//                        Stat stat = zooKeeper.exists(getLookIdPath(),);
//                        if(stat == null) {
//                            continue;
//                        }
                        synchronized (this) {
                            this.wait();
                        }

                }
            }
        } catch (KeeperException e) {
            throw new RuntimeException("lock occer exception,lockIdPath:"+getLookIdPath(),e);
        } catch (InterruptedException e) {
            return;
//            throw new RuntimeException("lock occer exception,lockIdPath:"+getLookIdPath(),e);
        }
        System.out.println("lock()"+Thread.currentThread().getName()+" cost:"+(System.currentTimeMillis() - start));
    }

    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public void unlock() {
//        createLockGroupPath(false);
        String unlockPath = lockPath;
        
        lockPath = null;
        long start = System.currentTimeMillis();
        try {
            localhostLock.unlock();
            if(unlockPath != null) { //test for lock seccess
                zooKeeper.delete(unlockPath, -1);
            }
        } catch (InterruptedException e) {
            //ignore unlock exception
            log.error("unlock occer exception",e);
        } catch (KeeperException e) {
          //ignore unlock exception
            log.error("unlock occer exception",e);
        }
        System.out.println("unlock() cost:"+(System.currentTimeMillis() - start));
    }

    
}
