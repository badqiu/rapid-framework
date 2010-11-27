package cn.org.rapid_framework.generator.provider.java.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

import cn.org.rapid_framework.generator.provider.java.model.JavaMethod.JavaMethodInvokeSequencesParser;

import junit.framework.TestCase;

public class JavaMethodTest extends TestCase {
	
	public void test() throws Exception{
		Method method = String.class.getMethod("valueOf",Object.class);
		JavaMethod m = new JavaMethod(method,new JavaClass(String.class));
		System.out.println(m.getParameters());
		assertFalse(m.isPropertyMethod());
	}
	
	public void test_isPropertyMethod() throws Exception{
		Method method = JavaMethod.class.getMethod("isSynthetic");
		JavaMethod m = new JavaMethod(method,new JavaClass(JavaMethod.class));
		System.out.println(m.getParameters());
		
		assertTrue(m.isPropertyMethod());
		
		method = JavaMethodTest.class.getMethod("isVoidMethod");
		m = new JavaMethod(method,new JavaClass(JavaMethodTest.class));
		System.out.println(m.getParameters());
		
		assertFalse(m.isPropertyMethod());
	}
	
	public void isVoidMethod() {
		
	}
	
	public void test_findWrapCharEndLocation() {
		int[] beginAndEnd = JavaMethodInvokeSequencesParser.findWrapCharEndLocation("0123{56}}", '{', '}');
		assertEquals(beginAndEnd[0],4);
		assertEquals(beginAndEnd[1],7);
		
		beginAndEnd = JavaMethodInvokeSequencesParser.findWrapCharEndLocation("0123{{67}}", '{', '}');
		assertEquals(beginAndEnd[0],4);
		assertEquals(beginAndEnd[1],9);

		beginAndEnd = JavaMethodInvokeSequencesParser.findWrapCharEndLocation("0123{{67}}}}", '{', '}');
		assertEquals(beginAndEnd[0],4);
		assertEquals(beginAndEnd[1],9);
		
		beginAndEnd = JavaMethodInvokeSequencesParser.findWrapCharEndLocation("0123{56\n}}}", '{', '}');
		assertEquals(beginAndEnd[0],4);
		assertEquals(beginAndEnd[1],8);
		
		//start test with return null
		beginAndEnd = JavaMethodInvokeSequencesParser.findWrapCharEndLocation("012356}}", '{', '}');
		assertNull(beginAndEnd);
		
		beginAndEnd = JavaMethodInvokeSequencesParser.findWrapCharEndLocation("0123{{{67}}", '{', '}');
		assertNull(beginAndEnd);
		
		beginAndEnd = JavaMethodInvokeSequencesParser.findWrapCharEndLocation("012367", '{', '}');
		assertNull(beginAndEnd);
	}
}
