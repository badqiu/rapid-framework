package cn.org.rapid_framework.util;

import java.util.concurrent.CountDownLatch;

import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

public class MultiThreadTestUtilsTest extends TestCase {
	private AtomicInteger executedCount = new AtomicInteger();
	int expectedCount = 2000;
	public void testExecute() throws InterruptedException {
		CountDownLatch doneSignel = MultiThreadTestUtils.execute(expectedCount, new Runnable() {
			public void run() {
				executedCount.getAndIncrement();
			}
		});
		
		doneSignel.await();
		
		assertEquals(expectedCount,executedCount.intValue());
	}
	
	public void testExecuteFail() throws InterruptedException {
		CountDownLatch doneSignel = MultiThreadTestUtils.execute(expectedCount, new Runnable() {
			public void run() {
				executedCount.getAndIncrement();
			}
		});
		
		System.out.println(executedCount);
		assertTrue(executedCount.intValue() < expectedCount);
	}
}
