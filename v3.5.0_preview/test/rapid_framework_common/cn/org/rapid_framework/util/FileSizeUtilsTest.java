package cn.org.rapid_framework.util;

import junit.framework.TestCase;

public class FileSizeUtilsTest extends TestCase {
	
	public void testGetHumanReadableFileSize() {
		assertEquals("0",FileSizeUtils.getHumanReadableFileSize(0));
		assertEquals("1.12KB",FileSizeUtils.getHumanReadableFileSize(1024+124+1));
		assertEquals("2MB",FileSizeUtils.getHumanReadableFileSize(1024*1024*2+8));
		assertEquals("1GB",FileSizeUtils.getHumanReadableFileSize(1024*1024*1024));
		long tb = (long)1024*1024*1024*1024*5;
		assertEquals("5TB",FileSizeUtils.getHumanReadableFileSize(tb+8));
		assertEquals("8192PB",FileSizeUtils.getHumanReadableFileSize(Long.MAX_VALUE));
		
		assertEquals("-1",FileSizeUtils.getHumanReadableFileSize(-1));
		assertEquals("-1000000",FileSizeUtils.getHumanReadableFileSize(-1000000));
	}
}
