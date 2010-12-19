


package cn.org.rapid_framework.generator;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cn.org.rapid_framework.generator.util.FileHelper;


public class GeneratorTest extends Assert{

    private Mockery  context = new JUnit4Mockery(){
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    protected Generator generator = new Generator();

    
    //dependence class
    String excludes = "";
    String includes = "";
    String outputEncoding = "";
    String removeExtensions = "";
    String sourceEncoding = "";
    File templateRootDir = null;
    File[] templateRootDirs = new File[]{};
    
    String tempOutDir = System.getProperty("java.io.tmpdir")+"/for_test_question_nation";
    
    Map templateModel = new HashMap();
    Map filePathModel = new HashMap();
    @Before
    public void setUp() throws Exception {
    	filePathModel.put("blogname", "BADQIU");
    	
        generator.setExcludes(excludes);
        generator.setIncludes(includes);
//        generator.setOutputEncoding(outputEncoding);
//        generator.setRemoveExtensions(removeExtensions);
//        generator.setSourceEncoding(sourceEncoding);
        generator.setTemplateRootDir(templateRootDir);
        generator.setTemplateRootDirs(templateRootDirs);
        
        // �뽫 context.checking(new Expectations(){ }) ��ط���Ǩ��������ĸ������Է�����.
        // ����ע�͵��ķ������Ը����Ҫ�ֹ�����ʹ�ã�����Ҫ����ɾ��
        /*
        */
        generator.setOutRootDir(tempOutDir);
    }
    
    @After
    public void tearDown() throws Throwable{
        context.assertIsSatisfied();
        FileHelper.deleteDirectory(new File(tempOutDir));
    }
    
    @Test(expected=IllegalStateException.class)
    public void test_deleteOutRootDir() throws Throwable{
    	generator.setOutRootDir("  ");
        generator.deleteOutRootDir();
    }
    
    @Test
    public void test_setTemplateRootDir() throws Throwable{
        
        
        File templateRootDir = null;
        
        generator.setTemplateRootDir(templateRootDir);
        
    }
    
    @Test
    public void test_addTemplateRootDir() throws Throwable{
        
        
        
        File f = new File(".");
        
        generator.addTemplateRootDir(f);
        
    }
    
    @Test
    public void test_setCopyBinaryFile() throws Throwable{
        
        
        boolean isCopyBinaryFile = true;
        
        generator.setCopyBinaryFile(isCopyBinaryFile);
        
    }
    
    @Test
    public void test_generateBy_for_test_question_nation() throws Throwable{
        
        System.getProperties().list(System.out);
		
        generator.setTemplateRootDir(FileHelper.getFileByClassLoader("for_test_question_nation"));
        
        Generator result = generator.generateBy(templateModel ,filePathModel );
        
        assertTrue(new File(tempOutDir,"Green").exists());
        assertTrue(new File(tempOutDir,"badqiu.java").exists());
    }
    
    @Test
    public void test_generateBy_removeExtensions() throws Throwable{
        
        

        System.getProperties().list(System.out);
        generator.setRemoveExtensions("bad,diy");
        generator.setTemplateRootDir(FileHelper.getFileByClassLoader("for_test_question_nation"));
        
        filePathModel.put("blogname", "BADQIU");
        generator.generateBy(templateModel ,filePathModel );
        
        assertTrue(new File(tempOutDir,"Green").exists());
        assertTrue(new File(tempOutDir,"BADQIU").exists());
    }
    
    @Test
    public void test_zip_file() throws Throwable{
    	
    	generator.setTemplateRootDir(FileHelper.getFileByClassLoader("for_test_zip/for_test_zip.zip"));
    	
    	generator.generateBy(templateModel ,filePathModel );
    	assertTrue(new File(tempOutDir,"folder/Green").exists());
        assertTrue(new File(tempOutDir,"folder/BADQIU.bad").exists());
    }

    @Test
    public void test_zip_file_with_sub_folder() throws Throwable{
    	
    	generator.setTemplateRootDir(FileHelper.getFileByClassLoader("for_test_zip/for_test_zip.zip")+"!/folder");
    	
    	generator.generateBy(templateModel ,filePathModel );
    	assertTrue(new File(tempOutDir,"Green").exists());
        assertTrue(new File(tempOutDir,"BADQIU.bad").exists());
    }
    
    @Test
    public void test_jar_file() throws Throwable{
    	
    	File templateRoot = FileHelper.getFileByClassLoader("com/mysql/jdbc/log");
    	System.out.println(templateRoot);
    	System.out.println(new URL(templateRoot.getPath()).getPath());
		generator.setTemplateRootDir(templateRoot);
    	
    	generator.generateBy(templateModel ,filePathModel );
    	assertTrue(new File(tempOutDir,"CommonsLogger.class").exists());
        assertTrue(new File(tempOutDir,"NullLogger.class").exists());
        
        generator.setTemplateRootDir("classpath:com/mysql/jdbc/log");
        
        generator.generateBy(templateModel ,filePathModel );
        assertTrue(new File(tempOutDir,"CommonsLogger.class").exists());
        assertTrue(new File(tempOutDir,"NullLogger.class").exists());
    }
    
    @Test
    public void test_deleteBy() throws Throwable{
        
        
        Map templateModel = new HashMap();
        Map filePathModel = new HashMap();
        generator.setTemplateRootDir("/not_exist_828282");
        Generator result = generator.deleteBy(templateModel ,filePathModel );
        
        assertNotNull(result);
    }
    
    
}
