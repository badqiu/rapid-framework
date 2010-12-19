package cn.org.rapid_framework.generator.ext.tableconfig.model;

import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.ext.tableconfig.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.test.GeneratorTestHelper;

public class IbatisTableConfigTest extends GeneratorTestCase {
	
	public void test_gen_TableConfig() throws Exception {
		 
		 TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML("com.global.pkg", FileHelper.getFile("classpath:cn/org/rapid_framework/generator/ext/tableconfig/"), "user_info.xml");
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
		 assertContains(str,IOHelper.readFile(FileHelper.getFile("classpath:IbatisTableConfigTest/expectedIbatisDaoImpl.txt")));
		 assertContains(str,IOHelper.readFile(FileHelper.getFile("classpath:IbatisTableConfigTest/expectedUserInfoDao.txt")));
		 assertContains(str,IOHelper.readFile(FileHelper.getFile("classpath:IbatisTableConfigTest/expectedUserInfoDO.txt")));
		 assertContains(str,IOHelper.readFile(FileHelper.getFile("classpath:IbatisTableConfigTest/expectedUserInfoSqlMap.txt")));
		 
		 assertContains(str,"src/main/java/com/company/project/user_info/dataobject/UserInfoDO.java");
		 assertContains(str,"src/main/java/com/company/project/user_info/ibatis/IbatisUserInfoDAO.java");
		 assertContains(str,"src/main/resources/sqlmap/badqiu_test_module/UserInfo-sqlmap-mapping.xml");
		 assertContains(str,"src/main/java/com/company/project/user_info/daointerface/UserInfoDAO.java");
		 
	}
}
