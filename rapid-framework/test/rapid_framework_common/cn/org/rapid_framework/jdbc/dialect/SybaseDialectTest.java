package cn.org.rapid_framework.jdbc.dialect;

import junit.framework.Assert;

import org.junit.Test;


public class SybaseDialectTest {

	Dialect dialect = new SybaseDialect();
	@Test(expected=UnsupportedOperationException.class)
	public void getLimitString() {
		Assert.assertEquals("", dialect.getLimitString("select * from user", 0, 0));
		Assert.assertEquals("", dialect.getLimitString("select * from user", 0, 12));
		Assert.assertEquals("", dialect.getLimitString("select * from user", 12, 0));
		Assert.assertEquals("", dialect.getLimitString("select * from user", 12, 34));
	}
	
}
