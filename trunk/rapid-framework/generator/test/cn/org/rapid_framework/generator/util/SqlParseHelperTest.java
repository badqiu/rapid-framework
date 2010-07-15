package cn.org.rapid_framework.generator.util;

import java.util.Set;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;


public class SqlParseHelperTest extends TestCase{
	
	public void test_getTableNamesByQuery_with_single_table() {
		Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user");
		System.out.println(tableNames);
		verifyTableNames(tableNames,"user");
	}
	
	public void test_getTableNamesByQuery_with_multi_table() {
	    Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user,role");
		System.out.println(tableNames);
		verifyTableNames(tableNames,"user","role");
		
	    tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user,user_role,blog");
	    System.out.println(tableNames);
	    verifyTableNames(tableNames,"user","user_role","blog");
	}

    public void test_getTableNamesByQuery_with_multi_table2() {
        Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u,role r where abc=123");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user","role");
        
        tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u,role r,blog b");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user","role","blog");
    }
	   
    public void test_getTableNamesByQuery_with_join() {
        Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u left join role r");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user","role");
        
        tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u inner join role r");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user","role");
        
        tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u left join role r");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user","role");
    }
    
    public void test_verify_delete() {
    	Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("delete from user_Info where username=:abc");
    	 verifyTableNames(tableNames,"user_Info");
    }

    public void test_verify_update() {
    	Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("update user_info set username = :username where password = :password and age=:age and sex=:sex");
    	verifyTableNames(tableNames,"user_info");
    }

    public void test_verify_insert() {
    	Set<String> tableNames = SqlParseHelper.getTableNamesByQuery("insert into user_info values(:username,:password,:age,:sex,:userid,:blog)");
    	verifyTableNames(tableNames,"user_info");
    }
    
	private void verifyTableNames(Set<String> tableNames,String... expectedTableNames) {
		for(int i = 0; i < expectedTableNames.length; i++) {
			assertTrue("actual tableNames:"+tableNames.toString(),tableNames.contains(expectedTableNames[i]));
		}
	}
	
	
	public void test_getColumnNameByRightCondition() {
		String sql = "delete from user_Info where " +
				" user_param =>  :user and blog_param=#{blog} and sex_param<=  ${sex} and pwd_param!=#pwd# and content_param<>$content$"+
				" and Memoparam like #memo# and birth_date between #{min_birth_date} and #{max_birth_date}"+
				" and java_param in (#java#) and  t.prifix_param=#{prefix} and net_param => substring(#net#)";
		assertEquals("user_param",SqlParseHelper.getColumnNameByRightCondition(sql, "user"));
		assertEquals("blog_param",SqlParseHelper.getColumnNameByRightCondition(sql, "blog"));
		assertEquals("sex_param",SqlParseHelper.getColumnNameByRightCondition(sql, "sex"));
		assertEquals("pwd_param",SqlParseHelper.getColumnNameByRightCondition(sql, "pwd"));
		assertEquals("content_param",SqlParseHelper.getColumnNameByRightCondition(sql, "content"));
		assertEquals("Memoparam",SqlParseHelper.getColumnNameByRightCondition(sql, "memo"));
		assertEquals("birth_date",SqlParseHelper.getColumnNameByRightCondition(sql, "min_birth_date"));
		assertEquals("birth_date",SqlParseHelper.getColumnNameByRightCondition(sql, "max_birth_date"));
		assertEquals("java_param",SqlParseHelper.getColumnNameByRightCondition(sql, "java"));
		assertEquals("prifix_param",SqlParseHelper.getColumnNameByRightCondition(sql, "prefix"));
		assertEquals("net_param",SqlParseHelper.getColumnNameByRightCondition(sql, "net"));
	}
	
    public void test_convert2ParametersString() {
        String sql = " delete from user_Info where \n " +
                " user_param>=? and blog_param=? and sex_param<=  ? and pwd_param!=? and content_param<>? and sex2like like ?";
        assertEquals(" delete from user_Info where \n  user_param>=#userParam# and blog_param=#blogParam# and sex_param<=  #sexParam# and pwd_param!=#pwdParam# and content_param<>#contentParam# and sex2like like #sex2like#",SqlParseHelper.convert2NamedParametersSql(sql, "#","#"));
    }
    
    public void test_convert2ParametersString_by_insert() {
        String sql = " insert into userinfo ( " +
                " user_name,pass_word, sex, age, birth_date , content,nowdate )"
                + " values(?,?,123,?,sysdate,?,now() )";
        String expected = " insert into userinfo ( " +
        " username,password, sex, age, birth_date \n , content,nowdate"
        + " values(#username#,#password#,123,#birthDate#,sysdate,#content#,now(?))";
        String expected2 = "insert into (user_name,pass_word,sex,age,birth_date,content,nowdate) values (#userName#,#passWord#,123,#age#,sysdate,#content#,now())";
        assertEquals(expected2,SqlParseHelper.convert2NamedParametersSql(sql, "#","#"));
        
        try {
            SqlParseHelper.convert2NamedParametersSql("insert into userinfo (id,sex) values (?)","#","#");
            fail();
        }catch(Exception e) {
            assertTrue(true);
        }
    }
	public void test_get_sql() {
	    String t = SqlParseHelper.getParameterClassName("select * from user where username = :username|Integer and pwd = :pwd|SexEnum", "username");
	    assertEquals(t,"Integer");
	    
	    t = SqlParseHelper.getParameterClassName("select * from user where username = :username|Integer and pwd = :pwd|SexEnum", "pwd");
	    assertEquals(t,"SexEnum");
	}
	
}
