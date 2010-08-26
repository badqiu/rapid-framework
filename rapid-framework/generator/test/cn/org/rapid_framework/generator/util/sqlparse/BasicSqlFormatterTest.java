package cn.org.rapid_framework.generator.util.sqlparse;

import junit.framework.TestCase;

public class BasicSqlFormatterTest extends TestCase {
	BasicSqlFormatter f = new BasicSqlFormatter();

	public void test() {
		format("select * from userinfo u inner join role r \n\n\n on u.id = r.id inner join blog b on u.id = b.id where a=123 and diy=234");
		format("select username,password,blog from userinfo u inner join role r \n\n\n on u.id = r.id inner join blog b on u.id = b.id where a=123 and diy=234");
		format("select username,\n\nabc,password,blog from userinfo u inner join role r \n\n\n on u.id = r.id inner join blog b on u.id = b.id where a=123 and diy=234");
		format("create table user(username varchar(2) primary key,\ncontent bigint)");
		format("insert into user(user,name,pwd,id) values (123,345,'123','bbb')");
		format("update user set user='123',name=456 where user=1 and pwd=2");
		format("    SELECT count(*) FROM USER_INFO  WHERE USERNAME = '1' AND PASSWORD = '1'  AND BIRTH_DATE >= '2010-08-27 01:35:16.421' AND BIRTH_DATE <= '2010-08-27 01:35:16.421' AND SEX = 1 AND AGE = 1 ");
	}
	
	public String format(String sql) {
		System.out.println(f.format(sql).trim());
		return f.format(sql);
	}
}
