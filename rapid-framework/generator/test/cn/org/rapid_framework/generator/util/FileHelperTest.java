package cn.org.rapid_framework.generator.util;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;


public class FileHelperTest {
    @Test
    public void test_isBinary() throws IOException {
        assertFalse(FileHelper.isBinaryFile(new File("abc.xml")));
        assertTrue(FileHelper.isBinaryFile(new File(".zip")));
    }
    
}
