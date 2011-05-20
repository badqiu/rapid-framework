package cn.org.rapid_framework.test.util;

import junit.framework.TestCase;



public class CurrentMethodUtilsTest extends TestCase{
    
    public void test_java_bb() {
        assertEquals("test_java_bb",CurrentMethodUtils.getCurrentMethodName());
        assertEquals("cn.org.rapid_framework.test.util.CurrentMethodUtilsTest",CurrentMethodUtils.getCurrentClassName());
    }
    
}
