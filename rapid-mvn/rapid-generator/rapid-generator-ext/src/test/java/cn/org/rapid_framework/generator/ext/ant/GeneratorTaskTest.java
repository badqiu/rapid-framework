/**
 * created since 2010-11-9
 */
package cn.org.rapid_framework.generator.ext.ant;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.tools.ant.Project;

import cn.org.rapid_framework.generator.GeneratorConstants;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.GLogger;

/**
 * @author zhongxuan
 * @version $Id: GeneratorTaskTest.java,v 0.1 2010-11-9 下午07:35:10 zhongxuan Exp $
 */
public class GeneratorTaskTest extends GeneratorTestCase {
//    GeneratorTask task = new GeneratorTask();
//    Project project = new Project();
//    
//    public void setUp() throws Exception {
//        super.setUp();
//        GLogger.perfLogLevel = GLogger.DEBUG;
//        GeneratorProperties.setProperty("basepackage", "com.company.project.generatortasktest");
//        GeneratorProperties.setProperty(GeneratorConstants.GENERATOR_TOOLS_CLASS, "cn.org.rapid_framework.generator.util.StringHelper");
//        GeneratorProperties.setProperty("sequencesList","seq_user_id");
//        project.setBaseDir(FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig"));
//        putAllProperties(project,GeneratorProperties.getProperties());
//        project.setProperty("appName", "projectName");
//        task.setProject(project);
//        
//        task.setTableConfigInput(toInput("for_generate_by_sql_config"));
//        task.setTableConfigOutput(toOutput("/temp/tableConfig"));
//        
//        task.setSequenceInput(toInput("for_generate_by_table_config_set"));
//        task.setSequenceOutput(toOutput("/temp/sequence"));
//        
//        task.setOperationInput(toInput("for_generate_by_sql"));
//        task.setOperationOutput(toOutput("/temp/operation"));
//        
//        task.setTableInput(toInput("for_generate_by_table"));
//        task.setTableOutput(toOutput("/temp/table"));
//    }
//
//    private void putAllProperties(Project project,Properties properties) {
//        for(Object key : properties.keySet()) {
//            project.setProperty(key.toString(), properties.getProperty(key.toString()));
//        }
//    }
//
//    public void tearDown() throws Exception {
//        super.tearDown();
//    }
//    
//    public void testGenSingleTable() {
//        task.setTableConfigFiles("user_info.xml");
//        task.setGenInputCmd("user_info");
//        task.execute();
//    }
//
//    public void testGenAllTable() {
//        task.setTableConfigFiles("user_info.xml");
//        task.setGenInputCmd("*");
//        task.execute();
//    }
//
//    public void testGenBySeq() {
//        task.setTableConfigFiles("user_info.xml");
//        task.setGenInputCmd("seq");
//        task.execute();
//    }
//
//    public void testGenByTable() throws IOException {
//        task.setGenInputCmd("table user_info");
//        task.execute();
//    }
//    
//    public File toInput(String path) throws IOException {
//        File file =  FileHelper.getFileByClassLoader(path);
//        assertNotNull(file);
//        return file;
//    }
//    
//    public File toOutput(String path) throws IOException {
//        return  new File(path);
//    }
}
