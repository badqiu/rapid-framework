package cn.org.rapid_framework.generator.ext.tableconfig.model;

import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.ext.tableconfig.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.test.GeneratorTestHelper;

public class IbatisTableConfigTest extends GeneratorTestCase {
	
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
		 
		 g.addTemplateRootDir("classpath:generator/template/rapid/table_config/dal");
		 g.addTemplateRootDir("classpath:generator/template/rapid/share/dal");
		 
		 String str = GeneratorTestHelper.generateBy(g, Helper.newMapFromTableConfig(tableConfigSet.getBySqlName("user_info")));
		 System.out.println(str);
		 
		 assertContains(str,"public java.lang.Long insert(UserInfoDO userInfo) throws DataAccessException;");
		 assertContains(str,"public java.lang.Long insertWithFunction(UserInfoDO userInfo) throws DataAccessException;");
		 assertContains(str,"根据订单号查询订单");
		 assertContains(str,"public CountUsernameResult countUsername(Long userId ,com.iwallet.biz.common.util.money.Money age ,com.iwallet.biz.common.util.money.Money sex ,String maxUsername ,String minUsername) throws DataAccessException;");
	
		 assertContains(str,"用户ID 		db_column: USER_ID ");
		 assertContains(str,"private Money sex = new Money(0,0);");
		 assertContains(str,IOHelper.readFile(FileHelper.getFile("classpath:IbatisTableConfigTest/expectedIbatisDaoImpl.txt"),"UTF-8"));
		 assertContains(str,IOHelper.readFile(FileHelper.getFile("classpath:IbatisTableConfigTest/expectedUserInfoDao.txt"),"UTF-8"));
		 assertContains(str,IOHelper.readFile(FileHelper.getFile("classpath:IbatisTableConfigTest/expectedUserInfoDO.txt"),"UTF-8"));
		 assertContains(str,IOHelper.readFile(FileHelper.getFile("classpath:IbatisTableConfigTest/expectedUserInfoSqlMap.txt"),"UTF-8"));
		 
		 assertContains(str,"src/main/java/com/company/project/user_info/dataobject/UserInfoDO.java");
		 assertContains(str,"src/main/java/com/company/project/user_info/ibatis/IbatisUserInfoDAO.java");
		 assertContains(str,"src/main/resources/sqlmap/badqiu_test_module/UserInfo-sqlmap-mapping.xml");
		 assertContains(str,"src/main/java/com/company/project/user_info/daointerface/UserInfoDAO.java");
		 
	}
	
	
    public void test_gen_TableConfig_for_return_primitive_type() throws Exception {
         tableConfigSet = new TableConfigXmlBuilder().parseFromXML("com.global.pkg", FileHelper.getFile("classpath:cn/org/rapid_framework/generator/ext/tableconfig/"), "user_info.xml");
         g.addTemplateRootDir("classpath:generator/template/rapid/table_config/dal");
         g.addTemplateRootDir("classpath:generator/template/rapid/share/dal");
         
         GeneratorProperties.setProperty("java_typemapping.java.lang.Long", "long");
         String str = GeneratorTestHelper.generateBy(g, Helper.newMapFromTableConfig(tableConfigSet.getBySqlName("user_info")));
         System.out.println(str);
         assertContains(str,"   public long countByUserId(Long userId) throws DataAccessException {Number returnObject = (Number)getSqlMapClientTemplate().queryForObject(\"badqiu_test_app.UserInfo.countByUserId\",userId);if(returnObject == null) return (long)0; elsereturn returnObject.longValue();}");
         
    }
}
