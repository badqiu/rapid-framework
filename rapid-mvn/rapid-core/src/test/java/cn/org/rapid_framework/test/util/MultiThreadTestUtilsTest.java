package cn.org.rapid_framework.test.util;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.TestCase;

public class MultiThreadTestUtilsTest extends TestCase {
	private AtomicInteger executedCount = new AtomicInteger();
	int expectedCount = 2000;
	public void testExecute() throws InterruptedException {
		MultiThreadTestUtils.execute(expectedCount, new Runnable() {
			public void run() {
				executedCount.getAndIncrement();
			}
		});
		
		assertEquals(expectedCount,executedCount.intValue());
	}
	
	public void testExecuteFail() throws InterruptedException {
		MultiThreadTestUtils.execute(expectedCount, new Runnable() {
			public void run() {
				executedCount.getAndIncrement();
			}
		});
		
		System.out.println(executedCount);
		assertTrue(executedCount.intValue() < expectedCount);
	}
	
	public void testexecuteAndWaitForDone() throws InterruptedException {
		
		long costTime = MultiThreadTestUtils.executeAndWait(expectedCount, new Runnable() {
			public void run() {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		System.out.println("costTime:"+costTime);
		assertTrue(costTime > 0);
	}
	
	
	public void testMultiThreadPermenece() throws InterruptedException {
		Map map = new TreeMap();
		int steps = 100;
		for(int i = 1; i < 3000; i = i + steps) {
			steps = steps + (int)(steps * 0.2);
			long costTime = execute(i);
			System.out.println("threadCount:"+ i +" costTime:"+costTime+" nextStep:"+steps);
			map.put(costTime,i);
		}
		System.out.println(map);
	}

	long MAX_COUNT = 10000;
	private long execute(int threadCount) throws InterruptedException {
		final AtomicLong count = new AtomicLong(0);
		long costTime = MultiThreadTestUtils.executeAndWait(threadCount, new Runnable() {
			int selfCount = 0;
			public void run() {
				while(true) {
					if(count.incrementAndGet() > MAX_COUNT) {
						return;
					}
					for(int i = 0; i < 50000; i++) {
					}
				}
			}
		});
		return costTime;
	}
}
