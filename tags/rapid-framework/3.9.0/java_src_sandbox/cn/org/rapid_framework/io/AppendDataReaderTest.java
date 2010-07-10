package cn.org.rapid_framework.io;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;


public class AppendDataReaderTest extends TestCase{
	AppendDataReader reader;
	
	public void setUp() {
		reader = new AppendDataReader(new StringReader("1\n2\n3\n4\n"),new StringReader("app end\ndata "));
	}
	public void test_readLines() throws IOException {
		List lines = IOUtils.readLines(reader);
		System.out.println(lines);
		assertEquals("1",lines.get(0));
		assertEquals("2",lines.get(1));
		assertEquals("3",lines.get(2));
		assertEquals("4",lines.get(3));
		assertEquals("app end",lines.get(4));
		assertEquals("data ",lines.get(5));
	}
	
	public void test_with_null() throws IOException {
		try {
		reader = new AppendDataReader(null,new StringReader("append\ndata"));
		IOUtils.readLines(reader);
		fail();
		}catch(IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
		reader = new AppendDataReader(new StringReader("1\n2\n3\n4\n"),(String)null);
		IOUtils.readLines(reader);
		fail();
		}catch(IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
		reader = new AppendDataReader(null,(Reader)null);
		IOUtils.readLines(reader);
		fail();
		}catch(IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	public void test_read() throws IOException {
		int result = -1;
		while((result = reader.read()) != -1) {
			System.out.print((char)result);
		}
	}
}
