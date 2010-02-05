package cn.org.rapid_framework.jdbc.dialect;

import junit.framework.Assert;

import org.junit.Test;


public class DerbyDialectTest {

	Dialect dialect = new DerbyDialect();
	@Test(expected=UnsupportedOperationException.class)
	public void getLimitString() {
		Assert.assertEquals("", dialect.getLimitString("select * from user", 0, 0));
		Assert.assertEquals("", dialect.getLimitString("select * from user", 0, 12));
		Assert.assertEquals("", dialect.getLimitString("select * from user", 12, 0));
		Assert.assertEquals("", dialect.getLimitString("select * from user", 12, 34));		
	}
	@Test(expected=UnsupportedOperationException.class)
	public void getLimitStringWithPlaceHolader() {
		String OFFSET = ":offset";
		String LIMIT = ":limit";
		Assert.assertEquals("select * from user limit :limit", dialect.getLimitString("select * from user", 0,OFFSET, 0,LIMIT));
		Assert.assertEquals("select * from user limit :limit", dialect.getLimitString("select * from user", 0,OFFSET,12,LIMIT));
		Assert.assertEquals("select * from user limit :offset,:limit", dialect.getLimitString("select * from user", 12, OFFSET,0,LIMIT));
		Assert.assertEquals("select * from user limit :offset,:limit", dialect.getLimitString("select * from user", 12,OFFSET, 34,LIMIT));
	}
	
}
