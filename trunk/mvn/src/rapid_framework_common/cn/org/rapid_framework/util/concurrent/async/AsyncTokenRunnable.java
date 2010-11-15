package cn.org.rapid_framework.util.concurrent.async;

import java.util.concurrent.Callable;

/**
 * Runnable及Callable的代理类,用于代理执行Runnable.run()及Callable.call()方法,并将执行结果设置进token
 * @author badqiu
 * @see AsyncTokenUtils
 */
public class AsyncTokenRunnable implements Runnable {
	AsyncToken asyncToken ;
	Runnable targetRunnable;
	Callable targetCallable;
	
	public AsyncTokenRunnable(AsyncToken asyncToken,Runnable target) {
		if(asyncToken == null) throw new IllegalArgumentException("asyncToken must be not null");
		if(target == null) throw new IllegalArgumentException("target Runnable must be not null");
		this.asyncToken = asyncToken;
		this.targetRunnable = target;
	}
	
	public AsyncTokenRunnable(AsyncToken asyncToken,Callable target) {
		if(asyncToken == null) throw new IllegalArgumentException("asyncToken must be not null");
		if(target == null) throw new IllegalArgumentException("target Callable must be not null");
		this.asyncToken = asyncToken;
		this.targetCallable = target;
	}

	public AsyncToken getAsyncToken() {
		return asyncToken;
	}

	public void run() {
		try {
			if(targetRunnable == null) {
				asyncToken.setComplete(targetCallable.call());
			}else {
				targetRunnable.run();
				asyncToken.setComplete();
			}
		}catch(Exception e) {
			asyncToken.setFault(e);
		}
	}

}