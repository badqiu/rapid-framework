package cn.org.rapid_framework.util.concurrent.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * AsyncToken工具类方法,用executor执行任务并将执行结果设置进AsyncToken并返回
 * <pre>
 * 
 * </pre>
 * @author badqiu
 *
 */
public class AsyncTokenUtils {

	public static <T> AsyncToken<T> execute(Executor executor,final Callable<T> task) {
		final AsyncToken token = new AsyncToken();
		executor.execute(new Runnable() {
			public void run() {
				try {
					token.setComplete(task.call());
				} catch (Exception e) {
					token.setComplete();
				}
			}
		});
		return token;
	}
	
	public static <T> AsyncToken<T> execute(Executor executor,final Runnable task) {
		final AsyncToken token = new AsyncToken();
		executor.execute(new Runnable() {
			public void run() {
				try {
					task.run();
					token.setComplete();
				}catch(Exception e) {
					token.setFault(e);
				}
			}
		});
		return token;
	}
	
}
