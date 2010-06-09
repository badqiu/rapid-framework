package cn.org.rapid_framework.test.context;

import junit.framework.TestCase;


public class Junit3TestMethodContextTest extends TestCase {
    
    public void test_alibaba() {
        System.out.println("xxxx:"+getName());
        assertEquals("test_alibaba", getName());
    }

    public void test_foo() {
        System.out.println("xxxx:"+getName());
        assertEquals("test_foo", getName());
    }
    
}
