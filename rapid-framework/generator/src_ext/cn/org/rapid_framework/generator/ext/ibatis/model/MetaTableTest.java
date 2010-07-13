package cn.org.rapid_framework.generator.ext.ibatis.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.FileHelper;

public class MetaTableTest extends GeneratorTestCase {
    
    public void setUp() throws Exception {
        super.setUp();
        System.setProperty("gg.isOverride", "true");
        
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql_config"));
        g.setOutRootDir("./temp/generate_by_sql_config");
    }
    
    public void test() throws Exception {
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/ibatis/user_info.xml");
        MetaTable t = MetaTable.parseFromXML(new FileInputStream(file));
        GeneratorModel gm = newFromTable(t);
        g.generateBy(gm.templateModel, gm.filePathModel);
    }

    private GeneratorModel newFromTable(MetaTable t) {
        
        Map templateModel = new HashMap();
        templateModel.putAll(GeneratorProperties.getProperties());
        templateModel.put("table", t);
        setShareVars(templateModel);
        
        Map filePathModel = new HashMap();
        filePathModel.putAll(GeneratorProperties.getProperties());
        filePathModel.putAll(BeanHelper.describe(t));
        setShareVars(filePathModel);
        GeneratorModel gm = new GeneratorModel(templateModel,filePathModel);
        return gm;
    }
    
    private static void setShareVars(Map templateModel) {
        templateModel.putAll(System.getProperties());
        templateModel.put("env", System.getenv());
    }
}
