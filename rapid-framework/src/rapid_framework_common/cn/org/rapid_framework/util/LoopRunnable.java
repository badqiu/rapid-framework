package cn.org.rapid_framework.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Runnable的代理类,可以为Runnable提供循环执行功能.并提供stop(),pause(),resume()等控制方法以控制循环的执行
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

	public void stop() {
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
			while(running) {
				pausedIfRequired();
				
				try {
					delegate.run();
				}catch(Throwable e) {
					if(log.isWarnEnabled()) {
						log.warn("delegate Runnable occer exception",e);
					}
				}
				
				sleepIfRequired();
			}
		}finally {
			paused = false;
			running = false;
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
