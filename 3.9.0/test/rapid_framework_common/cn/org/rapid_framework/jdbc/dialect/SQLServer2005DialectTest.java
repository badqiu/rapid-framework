package cn.org.rapid_framework.jdbc.dialect;

import org.junit.Test;

import static org.junit.Assert.*;

public class SQLServer2005DialectTest {
	Dialect dialect = new SQLServer2005Dialect();
	@Test
	public void testDialect() {
		assertEquals("WITH query AS (SELECT TOP 100 PERCENT  ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __row_number__,  * from user) SELECT * FROM query WHERE __row_number__ BETWEEN 0 AND 0 ORDER BY __row_number__", dialect.getLimitString("select * from user", 0, 0));
		assertEquals("WITH query AS (SELECT TOP 100 PERCENT  ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __row_number__,  * from user) SELECT * FROM query WHERE __row_number__ BETWEEN 0 AND 12 ORDER BY __row_number__", dialect.getLimitString("select * from user", 0, 12));
		assertEquals("WITH query AS (SELECT TOP 100 PERCENT  ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __row_number__,  * from user) SELECT * FROM query WHERE __row_number__ BETWEEN 12 AND 12 ORDER BY __row_number__", dialect.getLimitString("select * from user", 12, 0));
		assertEquals("WITH query AS (SELECT TOP 100 PERCENT  ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as __row_number__,  * from user) SELECT * FROM query WHERE __row_number__ BETWEEN 12 AND 46 ORDER BY __row_number__", dialect.getLimitString("select * from user", 12, 34));
	}
}
