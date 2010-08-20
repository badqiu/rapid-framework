package cn.org.rapid_framework.util;

import junit.framework.TestCase;

public class BasicSqlFormatterTest extends TestCase {
	BasicSqlFormatter f = new BasicSqlFormatter();

	public void test() {
		format("select * from userinfo u inner join role r \n\n\n on u.id = r.id inner join blog b on u.id = b.id where a=123 and diy=234");
		format("create table user(username varchar(2) primary key,\ncontent bigint)");
	}
	
	public String format(String sql) {
		System.out.println(f.format(sql).trim());
		return f.format(sql);
	}
}
