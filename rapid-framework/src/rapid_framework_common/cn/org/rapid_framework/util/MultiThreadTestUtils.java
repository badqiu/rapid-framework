package cn.org.rapid_framework.util;

import java.util.concurrent.CountDownLatch;

/**
 * @author badqiu
 */
public class MultiThreadTestUtils {
	
	private MultiThreadTestUtils() {}
	
	public static void executeAndWaitForDone(int threadCount,final Runnable task) throws InterruptedException {
		CountDownLatch doneSignal = execute(threadCount, task);
		doneSignal.await();
	}
	
	/**
	 * @param threadCount
	 * @param threadDemeon
	 * @param task
	 * @return 返回doneSignal
	 */
	public static CountDownLatch execute(int threadCount,final Runnable task) {
		final CountDownLatch startSignal = new CountDownLatch(1);
		final CountDownLatch startedSignal = new CountDownLatch(threadCount);
		final CountDownLatch doneSignal = new CountDownLatch(threadCount);
		for(int i = 0; i < threadCount; i++) {
			Thread t = new Thread(){
				public void run() {
					startedSignal.countDown();
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
			
			t.start();
		}
		
		try {
			startedSignal.await();
		} catch (InterruptedException e) {
			//ignore
		}
		startSignal.countDown();
		return doneSignal;
	}

}
