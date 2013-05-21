package cn.org.rapid_framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 主要功能: 通过缓存系统时间,提升System.currentTimeMillis()的效率
 * 通过设置时间刷新间隔可以提升系统时间精度
 * 
 * @author badqiu
 *
 */
public class SystemTimer {
	private static Logger logger = LoggerFactory.getLogger(SystemTimer.class);
	
	private static int refreshTimeInterval = 100;
	
	private static long cachedCurrentTimeMillis = System.currentTimeMillis();
	
	/**
	 * 设置时间刷新间隔
	 * @param refreshInterval
	 */
	public static void setRefreshTimeInterval(int refreshInterval) {
		if(refreshInterval <= 0) {
			throw new IllegalArgumentException("refreshInterval > 0 must be true");
		}
		SystemTimer.refreshTimeInterval = refreshInterval;
	}
	
	/**
	 * 得到系统当前时间
	 * @return
	 */
	public static long currentTimeMillis() {
		return cachedCurrentTimeMillis;
	}
	
	static Thread systemTimerFreshThread = new Thread("SystemTimerFresh") {
		@Override
		public void run() {
			logger.info("SystemTimer refresh thread started,refreshTimeInterval:"+refreshTimeInterval);
			while(true) {
				cachedCurrentTimeMillis = System.currentTimeMillis();
				try {
					Thread.sleep(refreshTimeInterval);
				} catch (InterruptedException e) {
					logger.error("SystemTimer refresh thread exit",e);
					throw new RuntimeException(e);
				}
			}
		}
	};
	
	static {
		systemTimerFreshThread.start();
	}
	
}
