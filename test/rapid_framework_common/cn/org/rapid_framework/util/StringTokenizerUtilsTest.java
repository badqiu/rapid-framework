package cn.org.rapid_framework.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringTokenizerUtilsTest {
	
	@Test
	public void test() {
		String[] array = StringTokenizerUtils.split("a|b,c d", "|, ");
		assertEquals(array[0],"a");
		assertEquals(array[1],"b");
		assertEquals(array[2],"c");
		assertEquals(array[3],"d");
	}
}
