package cn.org.rapid_framework.util.pkg.annotation;

import java.lang.annotation.Annotation;

import junit.framework.TestCase;

public class PackageAnnotationTest extends TestCase{
    
    public void test() {
        Annotation[] anns = PackageAnnotationTest.class.getPackage().getAnnotations();
        for(Annotation a : anns) {
            System.out.println(a);
        }
    }
}
