package cn.org.rapid_framework.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

import junit.framework.TestCase;
import edu.emory.mathcs.backport.java.util.concurrent.ArrayBlockingQueue;

public class AsyncOutputStreamTest extends TestCase {

	ByteArrayOutputStream targetOutput = new ByteArrayOutputStream();
	OutputStream output = new AsyncOutputStream(targetOutput);
	public void test() throws IOException, InterruptedException {
		
		output.write("java".getBytes());
		output.write("diy".getBytes());
		output.write(65);
		output.close();
		
		assertEquals("javadiyA",new String(targetOutput.toByteArray()));
		
		try {
			output.write("jiy".getBytes());
			fail();
		}catch(IOException expected) {
		}
	}
	
	public void testNullPointerException() throws IOException {
		try {
			output.write(null);
			fail();
		}catch(NullPointerException expected) {
			assertTrue(true);
		}
		
	}
	
	public void testPerformence() throws IOException {
		for(int i = 0; i < 200000; i++) {
			output.write("c".getBytes());
		}
		output.close();
		System.out.println(new String(targetOutput.toByteArray()));
		assertEquals(200000,new String(targetOutput.toByteArray()).length());
	}
	
}
