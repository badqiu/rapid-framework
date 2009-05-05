package cn.org.rapid_framework.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author badqiu
 */
public interface AsyncExceptinHandler {
	
	public void handle(Throwable e);
	
	public static class DefaultAsyncExceptinHandler implements AsyncExceptinHandler {
		private static Log log = LogFactory.getLog(AsyncWriter.class);
		public void handle(Throwable e) {
			log.error("Exception during write(): " + e,e);
		}
	};
	
}