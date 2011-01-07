package net.xulingbo.zookeeper.synchronizing;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import net.xulingbo.zookeeper.TestMainClient;
import net.xulingbo.zookeeper.TestMainServer;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * Synchronizing
 * <p/>
 * Author By: junshan
 * Created Date: 2010-9-3 15:09:50
 */
public class Synchronizing extends TestMainClient {
    int size;
    String name;
    public static final Logger logger = Logger.getLogger(Synchronizing.class);

    /**
     * 构造函数
     *
     * @param connectString 服务器连接
     * @param root 根目录
     * @param size 队列大小
     */
    Synchronizing(String connectString, String root, int size) {
        super(connectString);
        this.root = root;
        this.size = size;

        if (zk != null) {
            try {
                Stat s = zk.exists(root, false);
                if (s == null) {
                    zk.create(root, new byte[0], Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
                }
            } catch (KeeperException e) {
                logger.error(e);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
        try {
            name = new String(InetAddress.getLocalHost().getCanonicalHostName().toString());
        } catch (UnknownHostException e) {
            logger.error(e);
        }

    }

    /**
     * 加入队列
     *
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */

    void addQueue() throws KeeperException, InterruptedException{
        zk.exists(root + "/start",true);
        zk.create(root + "/" + name, new byte[0], Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        synchronized (mutex) {
            List<String> list = zk.getChildren(root, false);
            if (list.size() < size) {
                mutex.wait();
            } else {
                zk.create(root + "/start", new byte[0], Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getPath().equals(root + "/start") && event.getType() == Event.EventType.NodeCreated){
            System.out.println("得到通知");
            super.process(event);
            doAction();
        }
    }

    /**
     * 执行其他任务
     */
    private void doAction(){
        System.out.println("同步队列已经得到同步，可以开始执行后面的任务了");
    }

    public static void main(String args[]) {
        //启动Server
        TestMainServer.start();
        int size = 1; 
        Synchronizing b = new Synchronizing(TestMainServer.connectString, "/synchronizing", size);
        try{
            b.addQueue();
        } catch (KeeperException e){
            logger.error(e);
        } catch (InterruptedException e){
            logger.error(e);
        }
    }
}

