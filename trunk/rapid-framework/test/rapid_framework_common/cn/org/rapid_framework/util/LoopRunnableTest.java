package cn.org.rapid_framework.util;

import java.sql.Timestamp;

import org.junit.Assert;
import org.junit.Test;


public class LoopRunnableTest {
	@Test
	public void test_all_in_one() throws InterruptedException {
		LoopRunnable lr = new LoopRunnable(new Runnable() {
			public void run() {
				System.out.println(new Timestamp(System.currentTimeMillis()));
			}
		});
		lr.setSleepInterval(100);
		
		Thread t = new Thread(lr);
		Assert.assertFalse(lr.isRunning());
		t.start();
		
		Thread.sleep(1000);
		Assert.assertTrue(lr.isRunning());
		
		Assert.assertFalse(lr.isPaused());
		lr.pause();
		Assert.assertTrue(lr.isPaused());
		
		Thread.sleep(1000);
		
		Assert.assertTrue(lr.isPaused());
		lr.resume();
		Assert.assertFalse(lr.isPaused());
		
		Thread.sleep(1000);
		lr.stop();
		Assert.assertFalse(lr.isRunning());
		
		Thread.sleep(1000);
		Assert.assertFalse(t.isAlive());
	}
}
