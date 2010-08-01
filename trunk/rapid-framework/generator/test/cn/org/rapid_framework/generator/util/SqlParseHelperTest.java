package cn.org.rapid_framework.generator.util;

import java.util.Set;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper.NameWithAlias;


public class SqlParseHelperTest extends TestCase{
	
	public void test_getTableNamesByQuery_with_single_table() {
		Set<NameWithAlias> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user");
		System.out.println(tableNames);
		verifyTableNames(tableNames,"user");
		
		tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user t");
		System.out.println(tableNames);
		verifyTableNames(tableNames,"user t");
		
		tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user as t");
		System.out.println(tableNames);
		verifyTableNames(tableNames,"user t");
	}
	
	public void test_getTableNamesByQuery_with_multi_table() {
	    Set<NameWithAlias> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user,role");
		System.out.println(tableNames);
		verifyTableNames(tableNames,"user","role");
		
	    tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user,user_role,blog");
	    System.out.println(tableNames);
	    verifyTableNames(tableNames,"user","user_role","blog");
	    
	    tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user t1,user_role t2,blog t3");
	    System.out.println(tableNames);
	    verifyTableNames(tableNames,"user t1","user_role t2","blog t3");
	    
	    tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user as t1,user_role as t2,blog as t3");
	    System.out.println(tableNames);
	    verifyTableNames(tableNames,"user t1","user_role t2","blog t3");
	}

    public void test_getTableNamesByQuery_with_multi_table2() {
        Set<NameWithAlias> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u,role r where abc=123");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user u","role r");
        
        tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u,role r,blog b");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user u","role r","blog b");
    }
	   
    public void test_getTableNamesByQuery_with_join() {
        Set<NameWithAlias> tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u left join role r on u.username=r.username");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user u","role r");
        
        tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u inner join role r on u.username = r.username left join blog as b on u.username = b.username");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user u","role r","blog b");
        
        tableNames = SqlParseHelper.getTableNamesByQuery("select * froM user u left join role r on u.username = b.username where 1 = 1");
        System.out.println(tableNames);
        verifyTableNames(tableNames,"user u","role r");
    }
    
    public void test_verify_delete() {
    	Set<NameWithAlias> tableNames = SqlParseHelper.getTableNamesByQuery("delete from user_Info where username=:abc");
    	 verifyTableNames(tableNames,"user_Info");
    }

    public void test_verify_update() {
    	Set<NameWithAlias> tableNames = SqlParseHelper.getTableNamesByQuery("update user_info set username = :username where password = :password and age=:age and sex=:sex");
    	verifyTableNames(tableNames,"user_info");
    }

    public void test_verify_insert() {
    	Set<NameWithAlias> tableNames = SqlParseHelper.getTableNamesByQuery("insert into user_info values(:username,:password,:age,:sex,:userid,:blog)");
    	verifyTableNames(tableNames,"user_info");
    }
    
	public void test_join_same_table() {
		Set<NameWithAlias> tableNames = SqlParseHelper.getTableNamesByQuery("select t1.*,t2.* from user_info t1 inner join user_info2 t2 on t1.username=t2.username where t1.user_id = ? and t2.username = ?");
		verifyTableNames(tableNames,"user_info t1","user_info2 t2");
	}
	
	private void verifyTableNames(Set<NameWithAlias> tableNames,String... expectedTableNames) {
		for(int i = 0; i < expectedTableNames.length; i++) {
			String expectedTableName = expectedTableNames[i];
			NameWithAlias expectedTable = SqlParseHelper.parseSqlAlias(expectedTableName);
			for(NameWithAlias s : tableNames) {
				if(s.getName().equalsIgnoreCase(expectedTable.getName())) {
					assertEquals(s.getName(),s.getAlias(),expectedTable.getAlias());
				}
			}
//			assertTrue("actual tableNames:"+tableNames.toString(),expected);
		}
	}
	
	public void test_from_closes() {
		assertEquals("user",SqlParseHelper.getFromClauses("select * from user"));
		assertEquals("user t inner join info b",SqlParseHelper.getFromClauses("select * from user t inner join info b"));
		assertEquals("user t inner join info b ",SqlParseHelper.getFromClauses("select * from user t inner join info b where a=1"));
		assertEquals("user t inner join info b",SqlParseHelper.getFromClauses("select * from user t inner join info b group by username"));
		assertEquals("user t inner join info b",SqlParseHelper.getFromClauses("select * from user t inner join info b group by username having username > 100"));
		assertEquals("user t inner join info b",SqlParseHelper.getFromClauses("select * from user t inner join info b order by username"));
		
		assertEquals("user t inner join info b  ",SqlParseHelper.getFromClauses("select * from user t inner join info b   order    by   username"));
		assertEquals("user t inner join info b   order username",SqlParseHelper.getFromClauses("select * from user t inner join info b   order username"));
		assertEquals("user t inner join info b",SqlParseHelper.getFromClauses("select * from user t inner join info b group     by username"));
		assertEquals("user t inner join info b group  username",SqlParseHelper.getFromClauses("select * from user t inner join info b group  username"));
	}

	public void test_from_closes_union() {
		//UNION INTERSECT MINUS sqlserver:EXCEPT
		assertEquals("user t inner join info b",SqlParseHelper.getFromClauses("select * from user t inner join info b union select * from user"));
		assertEquals("user t inner join info b",SqlParseHelper.getFromClauses("select * from user t inner join info b INTERSECT select * from user"));
		assertEquals("user t inner join info b",SqlParseHelper.getFromClauses("select * from user t inner join info b MINUS select * from user"));
		assertEquals("user t inner join info b",SqlParseHelper.getFromClauses("select * from user t inner join info b EXCEPT select * from user"));
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
		
		
		sql = "delete from user_Info where " +
		" username_param = lower(#username#) and password_param >= substring(1,2,#password#)";
		assertEquals("username_param",SqlParseHelper.getColumnNameByRightCondition(sql, "username"));
		assertEquals("password_param",SqlParseHelper.getColumnNameByRightCondition(sql, "password"));
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
        String expected2 = " insert into userinfo (  user_name,pass_word, sex, age, birth_date , content,nowdate ) values(#userName#,#passWord#,123,#age#,sysdate,#content#,now())";
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
