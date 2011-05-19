package cn.org.rapid_framework.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Runnable的代理类,可以为Runnable提供循环执行功能.并提供shutdown(),pause(),resume()等控制方法以控制循环的执行
 * 
 * 
 * 
 * <br />
 * 示例使用:
 * <pre>
 * LoopRunnable lr = new LoopRunnable(new Runnable() {
 *	public void run() {
 *		System.out.println(new Timestamp(System.currentTimeMillis()));
 *	}
 * });
 * lr.setSleepInterval(100);
 *
 * Thread t = new Thread(lr);
 * t.start();
 * </pre>
 * 
 * @author badqiu
 *
 */
public class LoopRunnable implements Runnable{
	Log log = LogFactory.getLog(LoopRunnable.class);
	Runnable delegate;
	private boolean running = false;
	private boolean paused = false;
	private long sleepInterval = 0;
	
	public LoopRunnable(Runnable delegate) {
		this.delegate = delegate;
	}

	public void shutdown() {
		running = false;
	}
	
	public void pause() {
		paused = true;
	}
	
	public void resume() {
		paused = false;
		synchronized (this) {
			this.notify();
		}
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public long getSleepInterval() {
		return sleepInterval;
	}

	public void setSleepInterval(long sleepTimeMillis) {
		this.sleepInterval = sleepTimeMillis;
	}

	public void run() {
		running = true;
		try {
			beforeStart();
			while(running) {
				pausedIfRequired();
				try {
					delegate.run();
				}catch(Exception e) {
					handleIterateFailure(e);
				}
				sleepIfRequired();
			}
		}finally {
			paused = false;
			running = false;
			afterShutdown();
		}
	}
	
	/**
	 * 回调方法,线程在开始执行的时候，可以在这里面做一些初始化的动作
	 */
	protected void beforeStart() {
	}
	/**
	 * 回调方法,当线程在退出的时候，可能需要清理资源
	 */
	protected void afterShutdown() {
	}

	protected void handleIterateFailure(Exception e) {
		if(log.isWarnEnabled()) {
			log.warn("delegate Runnable occer exception",e);
		}
	}

	private void sleepIfRequired() {
		if(sleepInterval > 0 && running && !paused) {
			try {
				Thread.sleep(sleepInterval);
			} catch (InterruptedException e) {
				//ignore
			}
		}
	}

	private void pausedIfRequired() {
		if(paused && running) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					//ignore
				}
			}
		}
	}
	
}
