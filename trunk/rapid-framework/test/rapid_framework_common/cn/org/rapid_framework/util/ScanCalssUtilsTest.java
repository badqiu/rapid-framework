package cn.org.rapid_framework.util;

import java.util.List;

import junit.framework.TestCase;

public class ScanCalssUtilsTest extends TestCase {
    
    public void test() {
        List<String> classes = ScanClassUtils.scanPackages("cn.org.rapid**");
        System.out.println(classes);
        
//        Class clazz;
    }
}
