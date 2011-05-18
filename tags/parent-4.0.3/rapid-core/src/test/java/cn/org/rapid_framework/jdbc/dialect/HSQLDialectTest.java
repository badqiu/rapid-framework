package cn.org.rapid_framework.jdbc.dialect;

import junit.framework.Assert;

import org.junit.Test;


public class HSQLDialectTest {

	Dialect dialect = new HSQLDialect();
	@Test
	public void getLimitString() {
		Assert.assertEquals("select top 0 * from user", dialect.getLimitString("select * from user", 0, 0));
		Assert.assertEquals("select top 12 * from user", dialect.getLimitString("select * from user", 0, 12));
		Assert.assertEquals("select limit 12 0 * from user", dialect.getLimitString("select * from user", 12, 0));
		Assert.assertEquals("select limit 12 34 * from user", dialect.getLimitString("select * from user", 12, 34));		
	}
	@Test
	public void getLimitStringWithPlaceHolader() {
		String OFFSET = ":offset";
		String LIMIT = ":limit";
		Assert.assertEquals("select top :limit * from user", dialect.getLimitString("select * from user", 0,OFFSET, 0,LIMIT));
		Assert.assertEquals("select top :limit * from user", dialect.getLimitString("select * from user", 0,OFFSET,12,LIMIT));
		Assert.assertEquals("select limit :offset :limit * from user", dialect.getLimitString("select * from user", 12, OFFSET,0,LIMIT));
		Assert.assertEquals("select limit :offset :limit * from user", dialect.getLimitString("select * from user", 12,OFFSET, 34,LIMIT));
	}
}
