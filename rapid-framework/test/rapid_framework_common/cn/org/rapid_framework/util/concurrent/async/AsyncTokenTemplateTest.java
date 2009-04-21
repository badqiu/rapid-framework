package cn.org.rapid_framework.util.concurrent.async;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import junit.framework.TestCase;

public class AsyncTokenTemplateTest extends TestCase {
	private Object RESULT = new Object();
	private boolean executedResult = false;
	final AsyncTokenTemplate template = new AsyncTokenTemplate();
	public void test() throws InterruptedException {
		final AsyncToken token = new AsyncToken();
		Thread thread = new Thread(new Runnable() {
			public void run() {
				template.execute(token,new Callable(){
					public Object call() throws Exception {
						Thread.sleep(1000 * 2);
						return RESULT;
					}
				});
			}
		});
		thread.start();
		
		Thread.sleep(1000);
		
		token.addResponder(new IResponder() {
			public void onFault(Throwable fault) {
				assertNull(fault);
			}
			public void onResult(Object result) {
				assertEquals(result,result);
				executedResult = true;
			}
		});
		
		assertFalse(executedResult);
		
		Thread.sleep(1000 + 500);
		
		assertTrue(executedResult);
	}
}
