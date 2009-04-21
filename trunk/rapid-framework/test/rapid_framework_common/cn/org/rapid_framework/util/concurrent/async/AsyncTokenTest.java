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

	public void testTokenGroup() throws InterruptedException {
		AsyncToken token = new AsyncToken("badqiu","badqiu-1");
		assertEquals(token.getTokenGroup(),"badqiu");
		assertEquals(token.getTokenName(),"badqiu-1");
		assertTrue(token.getTokenId()> 0);
		
		AsyncToken token2 = new AsyncToken();
		assertEquals(token.getTokenId()+1,token2.getTokenId());
		assertEquals(token2.getTokenGroup(),AsyncToken.DEFAULT_TOKEN_GROUP);
		assertEquals(token2.getTokenName(),"Token-1");
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
