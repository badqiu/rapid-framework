package cn.org.rapid_framework.generator.util;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;


public class FileHelperTest extends TestCase {
    public void test_isBinary() throws IOException {
        assertFalse(FileHelper.isBinaryFile(new File("abc.xml")));
        assertTrue(FileHelper.isBinaryFile(new File(".zip")));
    }
    
    public void test() {
    	assertEquals(FileHelper.toFilePathIfIsURL(new File("file://c:/123.txt")),"/c:/123.txt");
    	assertEquals(FileHelper.toFilePathIfIsURL(new File("c:/123456.txt")).replace('/', '\\'),"c:\\123456.txt");
	
		try {
			new URL("c://123456.txt");
			fail();
		} catch (MalformedURLException e) {
		}
    }
}
