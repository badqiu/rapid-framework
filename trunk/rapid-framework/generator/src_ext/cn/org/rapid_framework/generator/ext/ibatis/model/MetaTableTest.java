package cn.org.rapid_framework.generator.ext.ibatis.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorModelUtils;
import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfig.MetaSql;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.StringHelper;

public class MetaTableTest extends GeneratorTestCase {
    
    public void setUp() throws Exception {
        super.setUp();
        g.setOutRootDir("./temp/"+getClass().getSimpleName()+"/"+getName());
        GeneratorProperties.setProperty("appName", "rapid");
    }
    
    public void test_genereate_by_sql_config() throws Exception {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql_config"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/ibatis/user_info.xml");
        TableConfig t = TableConfig.parseFromXML(new FileInputStream(file));
        GeneratorModel gm = newFromTable(t);
        g.generateBy(gm.templateModel, gm.filePathModel);
        
    }

    public void test_generate_by_sql() throws Exception {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/ibatis/user_info.xml");
        TableConfig t = TableConfig.parseFromXML(new FileInputStream(file));
        for(Sql sql : t.getSqls()) {
            GeneratorModel gm = newFromSql(sql,t);
            g.generateBy(gm.templateModel, gm.filePathModel);
        }
    }
    
    public void test_include_sql_by_refid() throws Exception {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/ibatis/user_info.xml");
        TableConfig t = TableConfig.parseFromXML(new FileInputStream(file));
        System.out.println(t.includeSqls);
        MetaSql metaSql = t.includeSqls.get(0);
        assertEquals(metaSql.sql.trim(),"<![CDATA[ USER_ID ,USERNAME ,PASSWORD ,BIRTH_DATE ,SEX ,AGE  ]]>");
        for(Sql sql : t.getSqls()) {
            GeneratorModel gm = newFromSql(sql,t);
            g.generateBy(gm.templateModel, gm.filePathModel);
        }
    }

    public void test_include_sql_by_mybatis() throws Exception {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/ibatis/mybatis_user_info.xml");
        TableConfig t = TableConfig.parseFromXML(new FileInputStream(file));
        for(Sql sql : t.getSqls()) {
            GeneratorModel gm = newFromSql(sql,t);
            g.generateBy(gm.templateModel, gm.filePathModel);
        }
    }
    
	public void test_remove_table_prefix() {
		GeneratorProperties.setProperty("tableRemovePrefixes", "t_,v_");
		TableConfig sql = new TableConfig();
		sql.setSqlname("t_user_info");
		assertEquals("UserInfo",sql.getTableClassName());
		sql.setSqlname("v_user");
		assertEquals("User",sql.getTableClassName());
		
		sql.setSqlname("diy_user");
		assertEquals("DiyUser",sql.getTableClassName());
	}
	
    
	public static GeneratorModel newFromSql(Sql sql, TableConfig t) {
        Map templateModel = new HashMap();
        templateModel.putAll(GeneratorProperties.getProperties());
        templateModel.put("sql", sql);
        templateModel.put("tableConfig", t);
        setShareVars(templateModel);
        
        Map filePathModel = new HashMap();
        filePathModel.putAll(GeneratorProperties.getProperties());
        filePathModel.putAll(BeanHelper.describe(t));
        filePathModel.putAll(BeanHelper.describe(sql));
        setShareVars(filePathModel);
        GeneratorModel gm = new GeneratorModel(templateModel,filePathModel);
        return gm;
    }

    public static GeneratorModel newFromTable(TableConfig t) {
        
        Map templateModel = new HashMap();
        templateModel.putAll(GeneratorProperties.getProperties());
        templateModel.put("tableConfig", t);
        setShareVars(templateModel);
        
        Map filePathModel = new HashMap();
        filePathModel.putAll(GeneratorProperties.getProperties());
        filePathModel.putAll(BeanHelper.describe(t));
        setShareVars(filePathModel);
        GeneratorModel gm = new GeneratorModel(templateModel,filePathModel);
        return gm;
    }
    
    private static void setShareVars(Map templateModel) {
    	GeneratorModelUtils.setShareVars(templateModel);
    	templateModel.put("StringHelper", new StringHelper());
    }
}
