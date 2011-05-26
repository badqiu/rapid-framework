package cn.org.rapid_framework.io;

import java.io.IOException;
import java.io.StringWriter;

import junit.framework.TestCase;

public class AsyncWriterTest extends TestCase {
	
	public void test() throws IOException, InterruptedException {
		StringWriter stringWriter = new StringWriter();
		
		AsyncWriter writer = new AsyncWriter(stringWriter);
		writer.start();
		
		writer.append("javaeye");
		writer.append("abc");
		writer.append(null);
		
//		Thread.sleep(1000);
		writer.close();
		
		assertEquals("javaeyeabcnull",stringWriter.toString());
		
		try {
			writer.append("jiy");
			fail();
		}catch(IOException expected) {
		}
	}
	
	public void testPerformence() throws IOException {
		StringWriter stringWriter = new StringWriter();
		AsyncWriter writer = new AsyncWriter(stringWriter,100);
		writer.start();
		for(int i = 0; i < 200000; i++) {
			writer.append('c');
		}
		writer.close();
		System.out.println(stringWriter.toString().length());
		assertEquals(200000,stringWriter.toString().length());
	}
	
	public void testNotYetStart() {
		try {
			StringWriter stringWriter = new StringWriter();
			AsyncWriter writer = new AsyncWriter(stringWriter,100);
			writer.append('c');
			fail();
		}catch(IOException e) {
			System.out.println(e.getMessage());
			assertEquals(e.getMessage(),"must start() before wirte()");
		}
	}
}
