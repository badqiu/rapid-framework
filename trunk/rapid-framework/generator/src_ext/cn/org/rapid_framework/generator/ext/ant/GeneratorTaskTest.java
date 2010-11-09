/**
 * created since 2010-11-9
 */
package cn.org.rapid_framework.generator.ext.ant;

import java.io.IOException;
import java.util.Properties;

import org.apache.tools.ant.Project;

import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.util.FileHelper;

/**
 * @author zhongxuan
 * @version $Id: GeneratorTaskTest.java,v 0.1 2010-11-9 下午07:35:10 zhongxuan Exp $
 */
public class GeneratorTaskTest extends GeneratorTestCase {
    GeneratorTask task = new GeneratorTask();
    Project project = new Project();
    
    public void setUp() throws Exception {
        super.setUp();
        project.setBaseDir(FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/ibatis"));
        putAllProperties(project,GeneratorProperties.getProperties());
        task.setProject(project);
    }

    private void putAllProperties(Project project,Properties properties) {
        for(Object key : properties.keySet()) {
            project.setProperty(key.toString(), properties.getProperty(key.toString()));
        }
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void test() throws IOException {
        task.setTableInput(getDir("for_generate_by_sql"));
        task.setTableOutput(getDir2("/temp/table"));
        
        task.setSequenceInput(getDir("for_generate_by_sql_config"));
        task.setSequenceOutput(getDir2("/temp/sequence"));
        
        task.setOperationInput(getDir("for_generate_by_table_config_set"));
        task.setOperationOutput(getDir2("/temp/operation"));
        
        task.setTableConfigFiles("user_info.xml");
        task.setGenInputCmd("user_info");
        task.execute();
        
    }

    public String getDir(String path) throws IOException {
        return FileHelper.getFileByClassLoader(path).getAbsolutePath();
    }
    
    public String getDir2(String path) throws IOException {
        return  path;
    }
}
