package cn.org.rapid_framework.jdbc.dialect;

import org.junit.Test;

import static org.junit.Assert.*;

public class SQLServer2005DialectTest {
	Dialect dialect = new SQLServer2005Dialect();
	@Test
	public void testDialect() {
		assertEquals("WITH query AS (SELECT TOP 100 PERCENT  ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __hibernate_row_nr__,  * from user) SELECT * FROM query WHERE __hibernate_row_nr__ > 0 ORDER BY __hibernate_row_nr__", dialect.getLimitString("select * from user", 0, 0));
		assertEquals("select * from user limit 12", dialect.getLimitString("select * from user", 0, 12));
		assertEquals("select * from user limit 0 offset 12", dialect.getLimitString("select * from user", 12, 0));
		assertEquals("select * from user limit 34 offset 12", dialect.getLimitString("select * from user", 12, 34));
	}
}
