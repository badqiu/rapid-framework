package cn.org.rapid_framework.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

import junit.framework.TestCase;
import edu.emory.mathcs.backport.java.util.concurrent.ArrayBlockingQueue;

public class AsyncOutputStreamTest extends TestCase {

	ByteArrayOutputStream targetOutput = new ByteArrayOutputStream();
	AsyncOutputStream asyncOutput = new AsyncOutputStream(targetOutput);
	
	public void setUp() {
		asyncOutput.start();
	}
	public void test() throws IOException, InterruptedException {
		
		asyncOutput.write("java".getBytes());
		asyncOutput.write("diy".getBytes());
		asyncOutput.write(65);
		asyncOutput.close();
		
		assertEquals("javadiyA",new String(targetOutput.toByteArray()));
		
		try {
			asyncOutput.write("jiy".getBytes());
			fail();
		}catch(IOException expected) {
		}
	}
	
	public void testNullPointerException() throws IOException {
		try {
			asyncOutput.write(null);
			fail();
		}catch(NullPointerException expected) {
			assertTrue(true);
		}
		
	}
	
	public void testPerformence() throws IOException {
		for(int i = 0; i < 200000; i++) {
			asyncOutput.write("c".getBytes());
		}
		asyncOutput.close();
		System.out.println(new String(targetOutput.toByteArray()));
		assertEquals(200000,new String(targetOutput.toByteArray()).length());
	}
	
	public void testNotYetStart() {
		try {
			ByteArrayOutputStream targetOutput = new ByteArrayOutputStream();
			AsyncOutputStream asyncOutput = new AsyncOutputStream(targetOutput);
			asyncOutput.write(1);
			fail();
		}catch(IOException e) {
			System.out.println(e.getMessage());
			assertEquals(e.getMessage(),"must start() before wirte()");
		}
		
		try {
			ByteArrayOutputStream targetOutput = new ByteArrayOutputStream();
			AsyncOutputStream asyncOutput = new AsyncOutputStream(targetOutput);
			asyncOutput.write(new byte[]{1,2});
			fail();
		}catch(IOException e) {
			System.out.println(e.getMessage());
			assertEquals(e.getMessage(),"must start() before wirte()");
		}
	}
}
