


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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(JMock.class)
public class GeneratorTest{

    private Mockery  context = new JUnit4Mockery(){
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    protected Generator generator = new Generator();

    
    //dependence class
    String excludes = "";
    String includes = "";
    String outRootDir = "";
    String outputEncoding = "";
    String removeExtensions = "";
    String sourceEncoding = "";
    File templateRootDir = null;
    File[] templateRootDirs = new File[]{};
    
    @Before
    public void setUp() throws Exception {
        
        generator.setExcludes(excludes);
        generator.setIncludes(includes);
        generator.setOutRootDir(outRootDir);
        generator.setOutputEncoding(outputEncoding);
        generator.setRemoveExtensions(removeExtensions);
        generator.setSourceEncoding(sourceEncoding);
        generator.setTemplateRootDir(templateRootDir);
        generator.setTemplateRootDirs(templateRootDirs);
        
        // �뽫 context.checking(new Expectations(){ }) ��ط���Ǩ��������ĸ������Է�����.
        // ����ע�͵��ķ������Ը����Ҫ�ֹ�����ʹ�ã�����Ҫ����ɾ��
        /*
        */
    }
    
    @After
    public void tearDown() throws Throwable{
        context.assertIsSatisfied();
    }
    
    @Test(expected=IllegalStateException.class)
    public void test_deleteOutRootDir() throws Throwable{
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
    public void test_generateBy() throws Throwable{
        
        
        Map templateModel = new HashMap();
        Map filePathModel = new HashMap();
        
        Generator result = generator.generateBy(templateModel ,filePathModel );
        
        assertNotNull(result);
    }
    
    @Test
    public void test_deleteBy() throws Throwable{
        
        
        Map templateModel = new HashMap();
        Map filePathModel = new HashMap();
        
        Generator result = generator.deleteBy(templateModel ,filePathModel );
        
        assertNotNull(result);
    }
    
    
}
