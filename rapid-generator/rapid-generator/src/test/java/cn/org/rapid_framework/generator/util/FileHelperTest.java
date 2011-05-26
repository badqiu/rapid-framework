package cn.org.rapid_framework.generator.util;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;


public class FileHelperTest extends TestCase {
    public void test_isBinary() throws IOException {
        assertFalse(FileHelper.isBinaryFile(new File("abc.xml")));
        assertTrue(FileHelper.isBinaryFile(new File(".zip")));
    }
    
    public void test_toFilePathIfIsURL() {
    	assertEquals(FileHelper.toFilePathIfIsURL(new File("file://c:/123.txt")),"/c:/123.txt");
    	assertEquals(FileHelper.toFilePathIfIsURL(new File("c:/123456.txt")).replace('/', '\\'),"c:\\123456.txt");
	
		try {
			new URL("c://123456.txt");
			fail();
		} catch (MalformedURLException e) {
		}
    }
    
    String userHome = System.getProperty("user.home");
    String tempDir = System.getProperty("java.io.tmpdir");
    public void test_getFile() {
        FileHelper.getFile("classpath:cn/org/rapid_framework/generator/util/FileHelperTest.class");
        FileHelper.getFile("classpath:/cn/org/rapid_framework/generator/util/FileHelperTest.class");
        
        try {
            FileHelper.getFile("classpath:cn/org/rapid_framework/generator/util/FileHelperTest_not_exist.class");
            fail("not found file");
        }catch(RuntimeException e){
        }
        
        File file = FileHelper.getFile("pom.xml");
        assertTrue(file.exists());
        
        new File(tempDir,"test_getFile").mkdir();
        new File(tempDir,"test_getFile").deleteOnExit();
        
        file = FileHelper.getFile(tempDir+"/test_getFile");
        assertTrue(file.getAbsolutePath(),file.exists());
        
        file = FileHelper.getFile("file:"+tempDir+"/test_getFile");
        assertTrue(file.getAbsolutePath(),file.exists());
        
        file = FileHelper.getFile("file://"+tempDir+"/test_getFile");
        assertTrue(file.getAbsolutePath(),file.exists());
        
        try {
            file = FileHelper.getFile("classpath://"+tempDir+"/test_getFile");
            fail("not found file");
        }catch(Exception e) {
        }
        
    }
}
