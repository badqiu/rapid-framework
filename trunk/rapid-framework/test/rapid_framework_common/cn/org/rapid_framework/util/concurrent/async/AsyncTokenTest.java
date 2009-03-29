package cn.org.rapid_framework.util.concurrent.async;

import junit.framework.TestCase;
import cn.org.rapid_framework.util.concurrent.async.AsyncToken.UncaughtExceptionHandler;

public class AsyncTokenTest extends TestCase {

	Exception exception = new Exception();
	public void testOnFault() throws InterruptedException {
		AsyncToken token = new AsyncToken();
		token.setFault(exception);
		
		token.addResponder(new IResponder() {
			public void onFault(Throwable fault) {
				System.out.println("onFault()");
				assertEquals(fault,exception);
			}
			public void onResult(Object result) {
				assertNull(result);
			}
		});
		
		
		Thread.sleep(500);
		
	}
	
	public void testOnResult() throws InterruptedException {
		AsyncToken token = new AsyncToken();
		token.setComplete(exception);
		
		token.addResponder(new IResponder() {
			public void onFault(Throwable fault) {
				assertNull(fault);
			}
			public void onResult(Object result) {
				System.out.println("onResult()");
				assertEquals(result,exception);
			}
		});
		
		
		Thread.sleep(500);
		
	}
	
	public void testFireTwice() {
		AsyncToken token = new AsyncToken();
		token.setComplete(exception);
		try {
			token.setComplete(exception);
			fail();
		}catch(IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	public void testUncaughtExceptionHandler() throws InterruptedException {
		final RuntimeException caughtException = new RuntimeException();
		
		AsyncToken token = new AsyncToken();
		token.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(IResponder responder, Throwable e) {
				assertEquals(caughtException,e);
				System.out.println("caughtException");
			}
		});
		
		token.setComplete(exception);
		token.addResponder(new IResponder() {
			public void onFault(Throwable fault) {
				assertNull(fault);
			}
			public void onResult(Object result) {
				throw caughtException;
			}
		});
		
		Thread.sleep(500);
	}
}
