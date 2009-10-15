package cn.org.rapid_framework.util;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;


public class HsqlMemDataSourceFactoryBeanTest {
	
	@Test
	public void test() throws Exception {
		DataSource ds = (DataSource)new HsqlMemDataSourceFactoryBean().getObject();
		
		ds = (DataSource)new HsqlMemDataSourceFactoryBean(new ClassPathResource("fortest_spring/for_test_hsql_db.sql"),"UTF-8").getObject();
	}
	
}
