package cn.org.rapid_framework.generator.provider.db.model;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.GeneratorProperties;



public class ColumnTest  extends TestCase{
	public void testColumn() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//		Column c = new Column(new Table(),1,"int","user_name",1,2,true,true,true,true,"","remarks");
//		System.out.println(BeanHelper.describe(c));
	}
	public void testGetJavaType() {
		GeneratorProperties.setProperty("java_typemapping.java.lang.String", "testJavaStringType");
		Column c = new Column(new Table(),1,"int","user_name",1,2,true,true,true,true,"","remarks");
		assertEquals("testJavaStringType",c.getJavaType());
		
		c = new Column(new Table(),Types.NUMERIC,"int","user_name",1,2,true,true,true,true,"","remarks");
		assertEquals("Long",c.getJavaType());
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "testJavaLongType");
		assertEquals("testJavaLongType",c.getJavaType());
	}
	
	public void testGetSimpleJavaType() {
		Column c = new Column(new Table(),1,"int","user_name",1,2,true,true,true,true,"","remarks");
		assertEquals("java.lang.String",c.getJavaType());
		assertEquals("String",c.getSimpleJavaType());
		
		c = new Column(new Table(),Types.NUMERIC,"int","user_name",1,2,true,true,true,true,"","remarks");
		assertEquals("Long",c.getSimpleJavaType());
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "testJavaLongType");
		assertEquals("testJavaLongType",c.getSimpleJavaType());
		
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "abc.badqiu.testJavaLongType");
		assertEquals("abc.badqiu.testJavaLongType",c.getSimpleJavaType());
	}
}
