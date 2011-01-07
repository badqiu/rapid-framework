package net.xulingbo.zookeeper.locks;

import net.xulingbo.zookeeper.ZooKeeperClient;

import org.apache.zookeeper.ZooKeeper;

/**
 * �ֲ�ʽ���Ĺ���
 * 
 * @author zhongxuan
 * @version $Id: DistributedLockFactory.java,v 0.1 2011-1-7 ����03:01:00 zhongxuan Exp $
 */
public class DistributedLockFactory {
    private static ZooKeeper zooKeeper;
    
    public static DistributedLock newLock(String lockGroup,String lockId) {
        DistributedLock lock = new DistributedLock(getZooKeeper(),lockGroup,lockId);
        return lock;
    }
    
    private static ZooKeeper getZooKeeper() {
        return ZooKeeperClient.zooKeeper;
    }
    
}
