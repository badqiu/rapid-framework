package cn.org.rapid_framework.generator.util.sqlparse;

import junit.framework.TestCase;

public class NamedParameterUtilsTest extends TestCase {
	public void setUp() {
		System.out.println(""+getName()+"()");
	}
	
	public void test_get_parameters_placeholders() {
		String sourceSql = "select * from username=#{username} and password=$pwd$ and blog=:blog and diy=${diy} and sex=${sex}";
		ParsedSql sql = NamedParameterUtils.parseSqlStatement(sourceSql);
		System.out.println(sql.getParameterPlaceholders());
		assertEquals("[#{username}, $pwd$, :blog, ${diy}, ${sex}]",sql.getParameterPlaceholders().toString());
	}
	
	public void testSpringJdbc() {
		verify("select * from username=:username and password=:pwd","select * from username=? and password=?");
		verify("select * from username=&username and password=&pwd","select * from username=? and password=?");
	}
	
	public void testIbatis() {
		verify("select * from username=#username# and password=#pwd#","select * from username=? and password=?");
		
		verify("select * from username=#username[]# and password=#pwd[]#","select * from username=? and password=?");
		
		//FIXME 应该抛出异常for ibatis2
		try {
			verify("select * from username=#username and password=#pwd","select * from username=? and password=?");
			fail();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		verify("select * from username=username# and password=pwd#","select * from username=username# and password=pwd#");
		
		ParsedSql sql = NamedParameterUtils.parseSqlStatement("select * from username=#username[]# and password=#pwd[]#");
		assertEquals("username",sql.getParameterNames().get(0));
		assertEquals("pwd",sql.getParameterNames().get(1));
	}

	public void testIbatisDollor() {
		verify("select * from username=$username$ and password=$pwd$ limit #limit#","select * from username=? and password=? limit ?");
		
		//FIXME 应该抛出异常for ibatis2
		try {
			verify("select * from username=$username and password=$pwd","select * from username=? and password=?");
			fail();
		}catch(Exception e) {
			System.out.println(e);
		}
		
		verify("select * from username=username$ and password=pwd$","select * from username=username$ and password=pwd$");
		
	}
	
	public void testMybatis() {
		verify("select * from username=#{username} and password=#{pwd}","select * from username=? and password=?");
		
		try {
			verify("select * from username=#username} and password=#{pwd}","select * from username=? and password=?");
			fail();
		}catch(Exception e) {
		}

		try {
			verify("select * from username=#{username and password=#{pwd}","select * from username=? and password=?");
			fail();
		}catch(Exception e) {
		}
		
		try{
			verify("select * from username=#{username and password=#{pwd}","select * from username=? and password=?");
			fail();
		}catch(Exception e) {
		}		
	}

	public void testMybatisDolor() {
		verify("select * from username=${username} and password=${pwd}","select * from username=? and password=?");
		
		try {
			verify("select * from username=$username} and password=${pwd}","select * from username=? and password=?");
			fail();
		}catch(Exception e) {
		}

		try {
			verify("select * from username=${username and password=${pwd}","select * from username=? and password=?");
			fail();
		}catch(Exception e) {
		}
		
		try{
			verify("select * from username=${username and password=${pwd}","select * from username=? and password=?");
			fail();
		}catch(Exception e) {
		}		
	}
	
	public void verify(String sourceSql,String expected) {
		ParsedSql sql = NamedParameterUtils.parseSqlStatement(sourceSql);
		assertEquals(expected,NamedParameterUtils.substituteNamedParameters(sql));
	}
}
