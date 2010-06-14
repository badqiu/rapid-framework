package cn.org.rapid_framework.generator.provider.db.model;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.GeneratorProperties;



public class ColumnTest  extends TestCase{
	public void setUp() {
		GeneratorProperties.reload();
	}
	
	public void testColumn() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//		Column c = new Column(new Table(),1,"int","user_name",1,2,true,true,true,true,"","remarks");
//		System.out.println(BeanHelper.describe(c));
	}
	public void testGetJavaType() {
		GeneratorProperties.setProperty("java_typemapping.java.lang.String", "testJavaStringType");
		Column c = new Column(new Table(),1,"int","user_name",1,2,true,true,true,true,"","remarks");
		assertEquals("testJavaStringType",c.getJavaType());
		
		assertEquals("Long",newBigDecimal().getJavaType());
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "testJavaLongType");
		assertEquals("testJavaLongType",newBigDecimal().getJavaType());
	}
	private Column newBigDecimal() {
		return new Column(new Table(),Types.NUMERIC,"int","user_name",1,2,true,true,true,true,"","remarks");
	}
	
	public void test_GetSimpleJavaType() {
		Column c = new Column(new Table(),1,"int","user_name",1,2,true,true,true,true,"","remarks");
		assertEquals("java.lang.String",c.getJavaType());
		assertEquals("String",c.getSimpleJavaType());
		
		assertEquals("Long",newBigDecimal().getSimpleJavaType());
		
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "testJavaLongType");
		assertEquals("testJavaLongType",newBigDecimal().getSimpleJavaType());
		
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "int");
		assertEquals("int",newBigDecimal().getSimpleJavaType());
		
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "Integer");
		assertEquals("Integer",newBigDecimal().getSimpleJavaType());
		
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "abc.badqiu.testJavaLongType");
		assertEquals("abc.badqiu.testJavaLongType",newBigDecimal().getSimpleJavaType());
		
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "org.badqiu.UserInfo");
		assertEquals("org.badqiu.UserInfo",newBigDecimal().getSimpleJavaType());
		
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "badboy");
		assertEquals("badboy",newBigDecimal().getSimpleJavaType());
		
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "java.math.BigDecimal");
		assertEquals("java.math.BigDecimal",newBigDecimal().getSimpleJavaType());
	}
}
