


package cn.org.rapid_framework.generator;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    @Before
    public void setUp() throws Exception {
        
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
        
        
        Map templateModel = new HashMap();
        Map filePathModel = new HashMap();
        System.getProperties().list(System.out);
        
		
        generator.setTemplateRootDir(FileHelper.getFileByClassLoader("for_test_question_nation"));
        
        filePathModel.put("blogname", "BADQIU");
        Generator result = generator.generateBy(templateModel ,filePathModel );
        
        assertTrue(new File(tempOutDir,"Green").exists());
        assertTrue(new File(tempOutDir,"badqiu.java").exists());
    }
    
    @Test
    public void test_generateBy_removeExtensions() throws Throwable{
        
        
        Map templateModel = new HashMap();
        Map filePathModel = new HashMap();
        System.getProperties().list(System.out);
        generator.setRemoveExtensions("bad,diy");
        generator.setTemplateRootDir(FileHelper.getFileByClassLoader("for_test_question_nation"));
        
        filePathModel.put("blogname", "BADQIU");
        Generator result = generator.generateBy(templateModel ,filePathModel );
        
        assertTrue(new File(tempOutDir,"Green").exists());
        assertTrue(new File(tempOutDir,"BADQIU").exists());
    }
    
    @Test
    public void test_deleteBy() throws Throwable{
        
        
        Map templateModel = new HashMap();
        Map filePathModel = new HashMap();
        generator.setTemplateRootDir(new File("/not_exist_828282"));
        Generator result = generator.deleteBy(templateModel ,filePathModel );
        
        assertNotNull(result);
    }
    
    
}
