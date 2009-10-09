package cn.org.rapid_framework.freemarker.loader;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import cn.org.rapid_framework.freemarker.FreemarkerTemplateProcessor;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class DataSourceTemplateLoaderTest {
	DataSourceTemplateLoader loader = new DataSourceTemplateLoader();
	DriverManagerDataSource ds = new DriverManagerDataSource();
	Map model = new HashMap();
	Configuration conf = new Configuration();
	@Before
	public void setUp() throws Exception {
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8");
		ds.setUsername("root");
		ds.setPassword("123456");
		
		loader.setDataSource(ds);
		loader.setTableName("template");
		loader.setTemplateNameColumn("template_name");
		loader.setTemplateContentColumn("template_content");
		loader.setTimestampColumn("last_modified");
		loader.afterPropertiesSet();
		
		conf.setDefaultEncoding("UTF-8");
		conf.setTemplateLoader(loader);
		
		model.put("username", "qiu");
		model.put("sex", "男");
	}
	@Test
	public void test_get_template() throws Exception, TemplateException {
		testProcessTemplate();
		
		Thread.sleep(1000 * 5);
		
		System.out.println("**************************");
		testProcessTemplate();
	}
	
	private void testProcessTemplate() throws IOException {
		Template t = conf.getTemplate("/test/template.ftl");
		String out = FreemarkerTemplateProcessor.processTemplateIntoString(t, model);
		System.out.println(out);
	}
	
}
