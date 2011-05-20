package cn.org.rapid_framework.generator.ext.tableconfig.model;

import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.ext.tableconfig.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.test.GeneratorTestHelper;

public class MybatisTableConfigTest extends GeneratorTestCase {
	
	static TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML("com.global.pkg", FileHelper.getFile("classpath:cn/org/rapid_framework/generator/ext/tableconfig/"), "user_info.xml");
	
	public void test_gen_operation() throws Exception {
		 g.addTemplateRootDir("classpath:generator/template/rapid/operation/dal");
		 g.addTemplateRootDir("classpath:generator/template/rapid/share/dal");
		 
		 String result = ""; 
		 TableConfig tableConfig = tableConfigSet.getBySqlName("user_info");
		 for(Sql sql : tableConfig.getSqls()) {
			 result = result + "\n" + GeneratorTestHelper.generateBy(g, Helper.newMapFromSql(sql, tableConfig));
		 }
		 System.out.println(result);
		 
		 assertContains(result, "package com.company.project.user_info.operation.userinfo;");
		 assertContains(result, IOHelper.readFile(FileHelper.getFile("classpath:IbatisTableConfigTest/expectedParameterAndResult.txt"),"UTF-8"));
		 
	}
	
	public void test_gen_TableConfig() throws Exception {
		 
		 g.addTemplateRootDir("classpath:generator/template/rapid/table_config/dao_mybatis");
		 g.addTemplateRootDir("classpath:generator/template/rapid/share/dal");
		 
		 String str = GeneratorTestHelper.generateBy(g, Helper.newMapFromTableConfig(tableConfigSet.getBySqlName("user_info")));
		 System.out.println(str);
		 
		 assertContains(str,"public java.lang.Long insert(UserInfoDO userInfo) throws DataAccessException");
		 assertContains(str,"public java.lang.Long insertWithFunction(UserInfoDO userInfo) throws DataAccessException");
		 assertContains(str,"根据订单号查询订单");
		 assertContains(str,"public CountUsernameResult countUsername(Long userId ,com.iwallet.biz.common.util.money.Money age ,com.iwallet.biz.common.util.money.Money sex ,String maxUsername ,String minUsername) throws DataAccessException");
	
		 assertContains(str,"用户ID 		db_column: USER_ID ");
		 assertContains(str,"private Money sex = new Money(0,0);");
		 assertContains(str,IOHelper.readFile(FileHelper.getFile("classpath:MybatisTableConfigTest/expectedUserInfoDao.txt"),"UTF-8"));
		 assertContains(str,IOHelper.readFile(FileHelper.getFile("classpath:IbatisTableConfigTest/expectedUserInfoDO.txt"),"UTF-8"));
		 assertContains(str,IOHelper.readFile(FileHelper.getFile("classpath:MybatisTableConfigTest/expectedUserInfoMapper.xml"),"UTF-8"));
		 
		 assertContains(str,"src/main/java/com/company/project/user_info/dataobject/UserInfoDO.java");
		 assertContains(str,"src/main/java/com/company/project/user_info/dao/UserInfoDao.java");
		 
	}
}
