package cn.org.rapid_framework.test.hsql;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;


public class HSQLMemDataSourceFactoryBeanTest {
	
	@Test
	public void test() throws Exception {
		DataSource ds = (DataSource)new HSQLMemDataSourceFactoryBean().getObject();
		
		ds = (DataSource)new HSQLMemDataSourceFactoryBean(new ClassPathResource("fortest_spring/for_test_hsql_db.sql"),"UTF-8").getObject();
		
		HSQLMemDataSourceFactoryBean hds = new HSQLMemDataSourceFactoryBean();
		hds.setSqlScript("create table blog(id int);insert into blog values (1);");
		hds.getObject();
		
		Assert.assertTrue("must be create multi datasource",hds.getObject() != hds.getObject());
	}
	
}
