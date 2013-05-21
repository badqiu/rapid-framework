package cn.org.rapid_framework.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JVMUtil {
	static Logger logger = LoggerFactory.getLogger(JVMUtil.class);
	
	static List<FileLock> jvmFile = new ArrayList<FileLock>();
	static List<ServerSocket> jvmSockets = new ArrayList<ServerSocket>();
	
	/**
	 * 得到当时进程PID,如果没有得到，返回-1
	 * @return
	 */
	public static int getPid() {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		int indexOf = name.indexOf("@");
		if(indexOf > 0) {
			return Integer.parseInt(name.substring(0,indexOf));
		}else {
			return -1;
		}
	}
	
	/**
	 * 锁定一个临时文件，以便实现一台服务器只能启动一个进程   
	 * @param lockName 类名称
	 */
	public static void lockFileForOnlyProcess(Class lockName) {
		lockFileForOnlyProcess(lockName.getName());
	}
	
	/**
	 * 锁定一个临时文件，以便实现一台服务器只能启动一个进程   
	 * @param lockName 临时文件名称,不要带目录名称
	 */
	public static void lockFileForOnlyProcess(String lockName) {
		File file = new File(System.getProperty("java.io.tmpdir"),lockName+".lock");
		try {
			FileOutputStream output = new FileOutputStream(file);
			FileLock fileLock = output.getChannel().tryLock();
			if(fileLock == null) {
				logger.warn("文件:'"+file+"'已经被lock,进程已经启动,系统将退出");
				System.exit(1);
			}
			
			PrintStream printStream = new PrintStream(output);
			printStream.println(getPid());
			printStream.flush();
			
			jvmFile.add(fileLock);
			logger.info("成功lock文件:'"+file+"',用以避免程序被多次启动,pid:"+getPid());
		}catch(IOException e) {
			logger.warn("获得文件lock时异常:'"+file+"',系统退出",e);
			System.exit(2);
		}
	}
}
