package cn.org.rapid_framework.util;

import java.util.concurrent.CountDownLatch;

public class MultiThreadTestUtils {
	
	public void executeAndWaitForDone(final Runnable task,int threadCount,boolean threadDemeon) throws InterruptedException {
		CountDownLatch doneSignal = execute(task, threadCount, threadDemeon);
		doneSignal.await();
	}
	
	public void executeAndWaitForDone(final Runnable task,int threadCount) throws InterruptedException {
		executeAndWaitForDone(task, threadCount,Thread.currentThread().isDaemon());
	}
	
	public CountDownLatch execute(final Runnable task,int threadCount) {
		return execute(task, threadCount,Thread.currentThread().isDaemon());
	}
	
	/**
	 * @param task
	 * @param threadCount
	 * @param threadDemeon
	 * @return 返回doneSignal
	 */
	public CountDownLatch execute(final Runnable task,int threadCount,boolean threadDemeon) {
		final CountDownLatch startSignal = new CountDownLatch(1);
		final CountDownLatch doneSignal = new CountDownLatch(threadCount);
		for(int i = 0; i < threadCount; i++) {
			Thread t = new Thread(){
				public void run() {
					
					try {
						startSignal.await();
					}catch(InterruptedException e) {
						//ignore
					}
					
					try {
						task.run();
					}finally {
						doneSignal.countDown();
					}
					
				}
			};
			
			t.setDaemon(threadDemeon);
			t.start();
		}
		
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			//ignore
		}
		
		return doneSignal;
	}
}
