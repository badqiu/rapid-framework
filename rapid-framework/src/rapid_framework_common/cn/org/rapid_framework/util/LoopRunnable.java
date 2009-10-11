package cn.org.rapid_framework.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
		if(sleepInterval > 0) {
			try {
				Thread.sleep(sleepInterval);
			} catch (InterruptedException e) {
				//ignore
			}
		}
	}

	private void pausedIfRequired() {
		if(paused) {
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
