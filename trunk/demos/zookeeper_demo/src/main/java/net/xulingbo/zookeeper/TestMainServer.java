package net.xulingbo.zookeeper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.zookeeper.server.ZooKeeperServerMain;

/**
 * TestMainServer
 * <p/>
 * Author By: junshan
 * Created Date: 2010-9-3 16:17:21
 */
public class TestMainServer extends ZooKeeperServerMain {
    public static final int CLIENT_PORT = 2181;
    public static final String serverHost = "localhost";
    public static final String connectString = serverHost + ":" + TestMainServer.CLIENT_PORT;
        
    public static class MainThread extends Thread {
        final File confFile;
        final TestMainServer main;

        public MainThread(int clientPort) throws IOException {
            super("Standalone server with clientPort:" + clientPort);
            File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            confFile = new File(tmpDir, "zoo.cfg");

            FileWriter fwriter = new FileWriter(confFile);
            fwriter.write("tickTime=2000\n");
            fwriter.write("initLimit=10\n");
            fwriter.write("syncLimit=5\n");

            File dataDir = new File(tmpDir, "data");
            dataDir.mkdirs();
            String df = org.apache.commons.lang.StringUtils.replace(dataDir.toString(),"\\","/");
            fwriter.write("dataDir=" + df + "\n");

            fwriter.write("clientPort=" + clientPort + "\n");
            fwriter.write("maxSessionTimeout=2000\n");
            fwriter.flush();
            fwriter.close();

            main = new TestMainServer();
        }

        public void run() {
            String args[] = new String[1];
            args[0] = confFile.toString();
            try {
                main.initializeAndRun(args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void start() {
        try {
            MainThread main = null;
            main = new MainThread(CLIENT_PORT);
            main.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        TestMainServer.start();
        try {
        Thread.sleep(1000 * 3000);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
