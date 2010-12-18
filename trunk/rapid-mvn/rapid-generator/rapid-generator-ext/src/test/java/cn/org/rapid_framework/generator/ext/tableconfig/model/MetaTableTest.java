package cn.org.rapid_framework.generator.ext.tableconfig.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import cn.org.rapid_framework.generator.GeneratorConstants;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorModelUtils;
import cn.org.rapid_framework.generator.ext.tableconfig.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.ext.tableconfig.model.TableConfig.Convert2SqlsProecssor;
import cn.org.rapid_framework.generator.ext.tableconfig.model.TableConfig.SqlConfig;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.test.GeneratorTestHelper;

public class MetaTableTest extends GeneratorTestCase {
    
    public void setUp() throws Exception {
        super.setUp();
        g.setOutRootDir("./target/temp/"+getClass().getSimpleName()+"/"+getName());
        GeneratorProperties.setProperty("appName", "rapid");
        GLogger.perfLogLevel = GLogger.INFO;
    }
    
    public void test_genereate_by_sql_config() throws Exception {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql_config"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/user_info.xml");
        TableConfig t = new TableConfigXmlBuilder().parseFromXML(new FileInputStream(file));
        GeneratorModel gm = newFromTable(t);
        g.generateBy(gm.templateModel, gm.filePathModel);
        
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        for(Sql sql : t.getSqls()) {
            GeneratorModel sqlGM = MetaTableTest.newFromSql(sql,t);
            g.generateBy(sqlGM.templateModel, sqlGM.filePathModel);
        }
    }

    public void test_generate_by_user_info() throws Exception {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/user_info.xml");
        TableConfig t = new TableConfigXmlBuilder().parseFromXML(new FileInputStream(file));
        for(Sql sql : t.getSqls()) {
            GeneratorModel gm = newFromSql(sql,t);
            g.generateBy(gm.templateModel, gm.filePathModel);
        }
    }

    public void test_generate_by_user_info_freemarker() throws Exception {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql_config"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/user_info_freemarker.xml");
        TableConfig t = new TableConfigXmlBuilder().parseFromXML(new FileInputStream(file));
        GeneratorModel gm = newFromTable(t);
        g.generateBy(gm.templateModel, gm.filePathModel);
        
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        for(Sql sql : t.getSqls()) {
            GeneratorModel sqlGM = MetaTableTest.newFromSql(sql,t);
            g.generateBy(sqlGM.templateModel, sqlGM.filePathModel);
        }
    }
    
    public void testSetOperations() throws Exception {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/user_info.xml");
        TableConfig t = new TableConfigXmlBuilder().parseFromXML(new FileInputStream(file));
        GeneratorModel gm = newFromSql(new Convert2SqlsProecssor().toSql(t, getName()),t);
        g.generateBy(gm.templateModel, gm.filePathModel);
    }
    
    public void test_include_sql_by_refid() throws Exception {
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/user_info.xml");
        TableConfig t = new TableConfigXmlBuilder().parseFromXML(file);
        System.out.println(t.getIncludeSqls());
        SqlConfig metaSql = t.getIncludeSqls().get(0);
        assertEquals(metaSql.sql.trim(),"<![CDATA[ USER_ID ,USERNAME ,PASSWORD ,BIRTH_DATE ,SEX ,AGE  ]]>");
    }

    public void test_generate_by_mybatis_user_info() throws Exception {
    	g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql_config"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/mybatis_user_info.xml");
        TableConfig t = new TableConfigXmlBuilder().parseFromXML(file);
        GeneratorModel gm = newFromTable(t);
        g.generateBy(gm.templateModel, gm.filePathModel);
        
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_generate_by_sql"));
        for(Sql sql : t.getSqls()) {
            gm = newFromSql(sql,t);
            g.generateBy(gm.templateModel, gm.filePathModel);
        }
    }
    
	public void test_remove_table_prefix() {
		GeneratorProperties.setProperty(GeneratorConstants.TABLE_REMOVE_PREFIXES, "t_,v_");
		TableConfig tc = new TableConfig();
		tc.setSqlName("t_user_info");
		assertEquals("UserInfo",tc.getClassName());
		tc.setSqlName("v_user");
		assertEquals("User",tc.getClassName());
		
		tc.setSqlName("diy_user");
		assertEquals("DiyUser",tc.getClassName());
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
        t.setPackage("com.company.project");
        Map templateModel = new HashMap();
        templateModel.putAll(GeneratorProperties.getProperties());
        templateModel.put("tableConfig", t);
        setShareVars(templateModel);
        
        Map filePathModel = new HashMap();
        setShareVars(filePathModel);
        filePathModel.putAll(GeneratorProperties.getProperties());
        filePathModel.putAll(BeanHelper.describe(t));
        GeneratorModel gm = new GeneratorModel(templateModel,filePathModel);
        return gm;
    }
    
    public void test_gen_by_operation() throws Exception{
    	File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/user_info.xml");
    	TableConfig t = new TableConfigXmlBuilder().parseFromXML(new FileInputStream(file));
    	g.addTemplateRootDir("classpath:generator/template/rapid/operation/dal/src/main");
    	g.addTemplateRootDir("classpath:generator/template/rapid/share/dal");
    	String content = GeneratorTestHelper.generateBy(g,new Helper().getMapBySql(t, "testIncludeWhere"));
    	assertContains(content,"java/com/company/project/user_info/operation/userinfo/TestIncludeWhereQuery.java");
    	assertContains(content, "package com.company.project.user_info.operation.userinfo;");
    	assertContains(content, "/** 用户名 */");
    	assertContains(content, "用户信息表");
    	assertContains(content, "extends.*PageQuery");
    	
    	content = GeneratorTestHelper.generateBy(g,new Helper().getMapBySql(t, "testIncludeWhereWithNoPaging"));
    	assertContains("java/com/company/project/user_info/operation/userinfo/TestIncludeWhereWithNoPagingQuery.java");
    	assertContains(content, "/** 用户名 */");
    	assertNotContains(content, "extends.*PageQuery");
    	System.out.println(content);
    }
    
    public static class Helper {
		public Map getMapBySql(TableConfig t, String operation) {
			Sql sql = getSql(t,operation);
	    	Map map = newMapFromSql(sql,t);
			return map;
		}
	    
	    public Map newMapFromSql(Sql sql,TableConfig tableConfig) {
	    	Map map = new HashMap();
	    	map.putAll(BeanHelper.describe(sql));
	    	map.put("sql", sql);
	    	map.put("tableConfig", tableConfig);
	    	map.put("basepackage", tableConfig.getBasepackage());
	    	return map;
	    }

	    public Map newMapFromTableConfigSet(TableConfigSet tableConfigSet) {
	    	Map map = new HashMap();
	    	map.putAll(BeanHelper.describe(tableConfigSet));
	    	map.put("tableConfigSet", tableConfigSet);
	    	return map;
	    }
	    
	    private Sql getSql(TableConfig t, String name) {
			for(Sql sql : t.getSqls()) {
				if(sql.getOperation().equals(name)) {
					return sql;
				}
			}
			throw new IllegalArgumentException("not found sql with name:"+name);
		}
    }
	public static void setShareVars(Map templateModel) {
    	templateModel.putAll(GeneratorModelUtils.getShareVars());
    	templateModel.put("StringHelper", new StringHelper());
    	templateModel.put("basepackage", "com.company.project");
    }
}
