package cn.org.rapid_framework.util.concurrent.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * AsyncToken工具类方法,用executor执行任务并将执行结果设置进AsyncToken并返回
 * @author badqiu
 *
 */
@SuppressWarnings("all")
public class AsyncTokenUtils {
	/**
	 * 通过executor执行Callable,将callable的执行结果设置进token
	 */
	public static void execute(Executor executor,AsyncToken token,final Callable task) {
		executor.execute(new AsyncTokenRunnable(token,task));
	}
	/**
	 * 通过executor执行Runnable
	 */
	public static void execute(Executor executor,AsyncToken token,final Runnable task) {
		executor.execute(new AsyncTokenRunnable(token,task));
	}
	
	/**
	 * 通过executor执行Callable,将callable的执行结果设置进token
	 */
	public static AsyncToken execute(Executor executor,AsyncTokenFactory factory,final Callable task) {
		AsyncToken token = factory.newToken();
		executor.execute(new AsyncTokenRunnable(token,task));
		return token;
	}
	/**
	 * 通过executor执行Runnable
	 */
	public static AsyncToken execute(Executor executor,AsyncTokenFactory factory,final Runnable task) {
		AsyncToken token = factory.newToken();
		executor.execute(new AsyncTokenRunnable(token,task));
		return token;
	}
	
}
