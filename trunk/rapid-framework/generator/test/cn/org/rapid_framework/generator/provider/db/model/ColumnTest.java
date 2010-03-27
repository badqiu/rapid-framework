package cn.org.rapid_framework.generator.provider.db.model;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import org.junit.Assert;
import org.junit.Test;

import cn.org.rapid_framework.generator.GeneratorProperties;



public class ColumnTest {
	@Test
	public void testColumn() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//		Column c = new Column(new Table(),1,"int","user_name",1,2,true,true,true,true,"","remarks");
//		System.out.println(BeanHelper.describe(c));
	}
	@Test
	public void testGetJavaType() {
		GeneratorProperties.setProperty("java_typemapping.java.lang.String", "testJavaStringType");
		Column c = new Column(new Table(),1,"int","user_name",1,2,true,true,true,true,"","remarks");
		Assert.assertEquals("testJavaStringType",c.getJavaType());
		
		c = new Column(new Table(),Types.NUMERIC,"int","user_name",1,2,true,true,true,true,"","remarks");
		Assert.assertEquals("Long",c.getJavaType());
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "testJavaLongType");
		Assert.assertEquals("testJavaLongType",c.getJavaType());
	}
}
