package cn.org.rapid_framework.generator.provider.db.sql.model;

import junit.framework.TestCase;

public class SqlParameterTest extends TestCase {
	
	SqlParameter p = new SqlParameter();
	public void test_toListParam_listParam_true() {
		p.setListParam(true);
		p.setParameterClass("Long");
		assertEquals(p.getPreferredParameterJavaType(),"java.util.List<Long>");
		
		p.setParameterClass("long");
		assertEquals(p.getPreferredParameterJavaType(),"java.util.List<Long>");
		
		p.setParameterClass("java.lang.Set");
		assertEquals(p.getPreferredParameterJavaType(),"java.lang.Set");
		
		p.setParameterClass("String[]");
		assertEquals(p.getPreferredParameterJavaType(),"String[]");
	}
	
	public void test_toListParam_listParam_false() {
		p.setListParam(false);
		p.setParameterClass("Long");
		assertEquals(p.getPreferredParameterJavaType(),"Long");
		
		p.setParameterClass("long");
		assertEquals(p.getPreferredParameterJavaType(),"Long");
	}
}
