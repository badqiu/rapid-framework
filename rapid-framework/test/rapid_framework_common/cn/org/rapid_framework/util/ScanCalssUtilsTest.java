package cn.org.rapid_framework.util;

import java.util.List;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.GeneratorFacade;

public class ScanCalssUtilsTest extends TestCase {
    
    public void test() throws Exception {
        List<String> classes = ScanClassUtils.scanPackages("cn.org.rapid_framework**");
        System.out.println(classes);
        for(String className:classes){
            Class clazz = Class.forName(className);
            new GeneratorFacade().generateByClass(clazz, "template_clazz");
        }
    }
}
