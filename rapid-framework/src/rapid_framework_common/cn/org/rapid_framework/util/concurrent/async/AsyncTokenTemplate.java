package cn.org.rapid_framework.util.concurrent.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken.UncaughtExceptionHandler;

/**
 * AsyncToken模板方法,用于生成token,使用方法如下:
 * <pre>
 * public AsyncToken sendAsyncEmail() {
 *		final AsyncToken token = new AsyncToken();
 *		Thread thread = new Thread(new Runnable() {
 *			public void run() {
 *				template.execute(token,new Callable(){
 *					public Object call() throws Exception {
 *						sendEmail();
 *						//do other something
 *						return result;
 *					}
 *				});
 *			}
 *		});
 *		thread.start();
 *		return token;
 * }
 * </pre>
 * @author badqiu
 *
 */
public class AsyncTokenTemplate {

	public void execute(AsyncToken token,Callable task) {
		try {
			Object result = task.call();
			token.setComplete(result);
		} catch (Throwable e) {
			token.setFault(e);
		}
	}


	
}
