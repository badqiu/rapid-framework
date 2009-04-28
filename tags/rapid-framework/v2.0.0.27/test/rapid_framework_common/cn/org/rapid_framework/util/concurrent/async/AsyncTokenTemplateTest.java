package cn.org.rapid_framework.util.concurrent.async;

import java.util.Date;
import java.util.concurrent.Callable;

import junit.framework.TestCase;

public class AsyncTokenTemplateTest extends TestCase {
	private Object RESULT = new Object();
	private boolean executedResult = false;
	
	public void test() throws InterruptedException {
		final AsyncToken token = new AsyncToken();
		Thread thread = new Thread(new Runnable() {
			public void run() {
				AsyncTokenTemplate.execute(token,new Callable(){
					public Object call() throws Exception {
						Thread.sleep(1000 * 3);
						return RESULT;
					}
				});
			}
		});
		thread.start();
		
		Thread.sleep(1500);
		
		token.addResponder(new IResponder() {
			public void onFault(Exception fault) {
				assertNull(fault);
			}
			public void onResult(Object result) {
				assertEquals(result,result);
				executedResult = true;
			}
		});
		
		assertFalse(executedResult);
		
		Thread.sleep(2000 + 500);
		
		assertTrue(executedResult);
	}
	
	public void testTemplate() {
		AsyncToken<Date> token = new AsyncToken();
		token.addResponder(new IResponder<Date>(){
			public void onFault(Exception fault) {
				
			}

			public void onResult(Date result) {
				
			}
		});
		
		AsyncTokenTemplate.execute(token, new Callable<Date>() {
			public Date call() throws Exception {
				return null;
			}
		});
		
		int count = Integer.MAX_VALUE;
		System.out.println(count);
		System.out.println(++count);
	}
}
