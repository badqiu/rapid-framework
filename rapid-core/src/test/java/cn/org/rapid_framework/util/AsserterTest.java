package cn.org.rapid_framework.util;

import junit.framework.TestCase;

public class AsserterTest extends TestCase {

	public void test_notEmpty() {
		try {
			Asserter.hasLength("", new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		String str = null;
		try {
			Asserter.hasLength(str, new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		str = " ";
		Asserter.hasLength(str, new RuntimeException());
		
		str = "1";
		Asserter.hasLength(str, new RuntimeException());
	}

	public void test_notBlank() {
		try {
			Asserter.hasText("", new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		String str = null;
		try {
			Asserter.hasText(str, new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		str = " ";
		try {
			Asserter.hasText(str, new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		str = "\n\t\r\f";
		try {
			Asserter.hasText(str, new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		str = "1";
		Asserter.hasText(str, new RuntimeException());
	}
}
