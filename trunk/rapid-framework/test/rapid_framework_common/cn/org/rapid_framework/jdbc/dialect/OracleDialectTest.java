package cn.org.rapid_framework.jdbc.dialect;

import junit.framework.TestCase;

public class OracleDialectTest extends TestCase {
	
	OracleDialect dialect = new OracleDialect();
	public void test() {
		String sql = dialect.getLimitString("select * from user", 10, 100);
		assertEquals("select * from ( select row_.*, rownum rownum_ from ( select * from user ) row_ ) where rownum_ <= 110 and rownum_ > 10",sql);
		System.out.println(sql);
		
		String sql0limit = dialect.getLimitString("select * from user", 0, 100);
		assertEquals("select * from ( select * from user ) where rownum <= 100",sql0limit);
		System.out.println(sql0limit);
		
		String forUpdateSql = dialect.getLimitString("select * from user for update", 10, 100);
		assertEquals("select * from ( select row_.*, rownum rownum_ from ( select * from user ) row_ ) where rownum_ <= 110 and rownum_ > 10 for update",forUpdateSql);
		System.out.println(forUpdateSql);
	}
}
