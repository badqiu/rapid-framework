package cn.org.rapid_framework.util;

import junit.framework.TestCase;

public class AsserterTest extends TestCase {

	public void test_notEmpty() {
		try {
			Asserter.notEmpty("", new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		String str = null;
		try {
			Asserter.notEmpty(str, new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		str = " ";
		Asserter.notEmpty(str, new RuntimeException());
		
		str = "1";
		Asserter.notEmpty(str, new RuntimeException());
	}

	public void test_notBlank() {
		try {
			Asserter.notBlank("", new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		String str = null;
		try {
			Asserter.notBlank(str, new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		str = " ";
		try {
			Asserter.notBlank(str, new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		str = "\n\t\r\f";
		try {
			Asserter.notBlank(str, new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		str = "1";
		Asserter.notBlank(str, new RuntimeException());
	}
}
