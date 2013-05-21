package cn.org.rapid_framework.test.util;

import java.util.concurrent.CountDownLatch;

/**
 * 多线程测试的工具类,可以根据指定的线程数并发的同时执行同一个task用于单元测试使用
 * @author badqiu
 */
public class MultiThreadTestUtils {
	
	/**
	 * 执行测试并等待执行结束,返回值为消耗时间
	 * 
	 * @param threadCount 线程数
	 * @param task 任务
	 * @return costTime
	 * @throws InterruptedException 
	 */
	@Deprecated
	public static long executeAndWait(int threadCount,final Runnable task) throws InterruptedException {
		return execute(threadCount,task);
	}
	
	/**
	 * 执行测试并等待执行结束,返回值为消耗时间
	 * 
	 * @param threadCount 线程数
	 * @param task 任务
	 * @return costTime
	 * @throws InterruptedException 
	 */
	public static long execute(int threadCount,final Runnable task) throws InterruptedException {
		CountDownLatch doneSignal = execute0(threadCount, task);
		long startTime = System.currentTimeMillis();
		doneSignal.await();
		return System.currentTimeMillis() - startTime;
	}

	private static CountDownLatch execute0(int threadCount,final Runnable task) {
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
						return;
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
