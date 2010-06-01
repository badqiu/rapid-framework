package cn.org.rapid_framework.generator.util;


import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;


public class FileHelperTest extends TestCase {
    public void test_isBinary() throws IOException {
        assertFalse(FileHelper.isBinaryFile(new File("abc.xml")));
        assertTrue(FileHelper.isBinaryFile(new File(".zip")));
    }
    
}
