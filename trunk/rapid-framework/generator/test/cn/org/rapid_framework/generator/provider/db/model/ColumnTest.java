package cn.org.rapid_framework.generator.provider.db.model;

import java.lang.reflect.InvocationTargetException;
import java.sql.Types;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.provider.db.table.model.ForeignKey.ReferenceKey;



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
		assertEquals("testJavaLongType",newBigDecimal().getSimpleJavaType());
		
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "org.badqiu.UserInfo");
		assertEquals("UserInfo",newBigDecimal().getSimpleJavaType());
		
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "badboy");
		assertEquals("badboy",newBigDecimal().getSimpleJavaType());
		
		GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "java.math.BigDecimal");
		assertEquals("BigDecimal",newBigDecimal().getSimpleJavaType());
	}
	
	public void test_getShortJavaType() {
        Column c = new Column(new Table(), 1, "int", "user_name", 1, 2, true,
            true, true, true, "", "remarks");
        assertEquals("String", c.getShortJavaType());

        GeneratorProperties.setProperty(
            "java_typemapping.java.math.BigDecimal", "org.badqiu.UserInfo");
        assertEquals("UserInfo", newBigDecimal().getSimpleJavaType());
        assertEquals("UserInfo", newBigDecimal().getShortJavaType());
    }
	
	public void test_setForeignKey() {
		ReferenceKey ref = ReferenceKey.fromString("role(role_id)");
		assertEquals(ref.columnSqlName,"role_id");
		assertEquals(ref.tableName,"role");
		assertEquals(ref.schemaName,null);
		
		ref = ReferenceKey.fromString("security.role(role_id)");
		assertEquals(ref.columnSqlName,"role_id");
		assertEquals(ref.tableName,"role");
		assertEquals(ref.schemaName,"security");
		
		assertNull(ReferenceKey.fromString("   "));
		assertNull(ReferenceKey.fromString(null));
		assertNull(ReferenceKey.fromString(""));
		
		try {
		assertNull(ReferenceKey.fromString("role"));
		fail();
		}catch(IllegalArgumentException e){
			
		}
	}
	
	public void test_getJSR303Validation() {
		Column nullColumn = new Column(new Table(),Types.TINYINT,"int","email",1,2,false,true,false,false,"","remarks");
		System.out.println(nullColumn.getHibernateValidatorExprssion());
		assertEquals("@Email  @Max(127)",nullColumn.getHibernateValidatorExprssion());
		
		Column num = new Column(new Table(),Types.TINYINT,"int","email",1,2,false,false,false,false,"","remarks");
		System.out.println(num.getHibernateValidatorExprssion());
		assertEquals("@NotNull @Email  @Max(127)",num.getHibernateValidatorExprssion());
		
		Column str = new Column(new Table(),Types.VARCHAR,"int","email",1,2,false,false,false,false,"","remarks");
		System.out.println(str.getHibernateValidatorExprssion());
		assertEquals("@NotBlank @Email @Length(max=1)",str.getHibernateValidatorExprssion());
	}
	
	public void test_getNullValue() {
	    GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "Long");
	    assertEquals("Long",newBigDecimal().getSimpleJavaType());
        assertEquals("null",newBigDecimal().getNullValue());
        assertEquals(false,newBigDecimal().isHasNullValue());
        
	    Column num = newBigDecimal();
	    GeneratorProperties.setProperty("java_typemapping.java.math.BigDecimal", "int");
        assertEquals("int",newBigDecimal().getSimpleJavaType());
        assertEquals("0",newBigDecimal().getNullValue());
        assertEquals(true,newBigDecimal().isHasNullValue());
        System.out.println(newBigDecimal().getJavaType());
	    System.out.println(newBigDecimal().getNullValue());
	    System.out.println(newBigDecimal().isHasNullValue());
	}
}
