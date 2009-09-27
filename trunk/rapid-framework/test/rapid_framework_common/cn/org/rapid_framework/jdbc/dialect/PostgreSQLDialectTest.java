package cn.org.rapid_framework.jdbc.dialect;

import junit.framework.Assert;

import org.junit.Test;


public class PostgreSQLDialectTest {

	Dialect dialect = new PostgreSQLDialect();
	@Test
	public void getLimitString() {
		Assert.assertEquals("select * from user limit 0", dialect.getLimitString("select * from user", 0, 0));
		Assert.assertEquals("select * from user limit 12", dialect.getLimitString("select * from user", 0, 12));
		Assert.assertEquals("select * from user limit 0 offset 12", dialect.getLimitString("select * from user", 12, 0));
		Assert.assertEquals("select * from user limit 34 offset 12", dialect.getLimitString("select * from user", 12, 34));		
	}
	@Test
	public void getLimitStringWithPlaceHolader() {
		String OFFSET = ":offset";
		String LIMIT = ":limit";
		Assert.assertEquals("select * from user limit :limit", dialect.getLimitString("select * from user", 0,OFFSET, 0,LIMIT));
		Assert.assertEquals("select * from user limit :limit", dialect.getLimitString("select * from user", 0,OFFSET,12,LIMIT));
		Assert.assertEquals("select * from user limit :limit offset :offset", dialect.getLimitString("select * from user", 12, OFFSET,0,LIMIT));
		Assert.assertEquals("select * from user limit :limit offset :offset", dialect.getLimitString("select * from user", 12,OFFSET, 34,LIMIT));
	}
}
