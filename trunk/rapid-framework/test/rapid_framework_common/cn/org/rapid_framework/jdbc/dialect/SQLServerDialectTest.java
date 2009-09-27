package cn.org.rapid_framework.jdbc.dialect;

import junit.framework.Assert;

import org.junit.Test;


public class SQLServerDialectTest {

	Dialect dialect = new SQLServerDialect();
	@Test
	public void getLimitString() {
		Assert.assertEquals("select top 0 * from user", dialect.getLimitString("select * from user", 0, 0));
		Assert.assertEquals("select top 12 * from user", dialect.getLimitString("select * from user", 0, 12));
	}
	@Test(expected=UnsupportedOperationException.class)
	public void getLimitStringWithException1() {
		Assert.assertEquals("", dialect.getLimitString("select * from user", 12, 0));
	}
	@Test(expected=UnsupportedOperationException.class)
	public void getLimitStringWithException2() {
		Assert.assertEquals("", dialect.getLimitString("select * from user", 12, 34));
	}
	@Test
	public void getLimitStringWithPlaceHolader() {
		String OFFSET = ":offset";
		String LIMIT = ":limit";
		Assert.assertEquals("select top 0 * from user", dialect.getLimitString("select * from user", 0,OFFSET, 0,LIMIT));
		Assert.assertEquals("select top 12 * from user", dialect.getLimitString("select * from user", 0,OFFSET,12,LIMIT));
	}
}
