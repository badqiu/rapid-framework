package cn.org.rapid_framework.jdbc.dialect;

import junit.framework.Assert;

import org.junit.Test;


public class SybaseDialectTest {

	Dialect dialect = new SybaseDialect();
	@Test(expected=UnsupportedOperationException.class)
	public void getLimitString() {
		Assert.assertEquals("", dialect.getLimitString("select * from user", 0, 0));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void getLimitStringWithPlaceHolader() {
		String OFFSET = ":offset";
		String LIMIT = ":limit";
		Assert.assertEquals("select * from user limit :limit", dialect.getLimitString("select * from user", 0,OFFSET, 0,LIMIT));
	}
}
