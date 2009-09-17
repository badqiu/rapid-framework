package cn.org.rapid_framework.util.concurrent.async;

import java.util.concurrent.Callable;

/**
 * AsyncToken模板方法,用于生成token,使用方法如下:
 * <pre>
 * public AsyncToken sendAsyncEmail() {
 *		final AsyncToken token = new AsyncToken();
 *		Thread thread = new Thread(new Runnable() {
 *			public void run() {
 *				AsyncTokenUtils.execute(token,new Callable(){
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
public class AsyncTokenUtils {

	public static void execute(AsyncToken token,Callable task) {
		try {
			Object result = task.call();
			token.setComplete(result);
		} catch (Exception e) {
			token.setFault(e);
		}
	}
	
}
