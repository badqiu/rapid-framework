package cn.org.rapid_framework.util;

import java.util.concurrent.CountDownLatch;

public class MultiThreadTestUtils {
	private MultiThreadTestUtils() {}
	
	public static void executeAndWaitForDone(int threadCount,boolean threadDemeon,final Runnable task) throws InterruptedException {
		CountDownLatch doneSignal = execute(threadCount, threadDemeon, task);
		doneSignal.await();
	}
	
	public static void executeAndWaitForDone(int threadCount,final Runnable task) throws InterruptedException {
		executeAndWaitForDone(threadCount, Thread.currentThread().isDaemon(),task);
	}
	
	public static CountDownLatch execute(int threadCount,final Runnable task) {
		return execute(threadCount, Thread.currentThread().isDaemon(),task);
	}
	
	/**
	 * @param threadCount
	 * @param threadDemeon
	 * @param task
	 * @return 返回doneSignal
	 */
	public static CountDownLatch execute(int threadCount,boolean threadDemeon,final Runnable task) {
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
		
		sleepForThreadsStarted();
		
		return doneSignal;
	}

	private static void sleepForThreadsStarted() {
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			//ignore
		}
	}
}
