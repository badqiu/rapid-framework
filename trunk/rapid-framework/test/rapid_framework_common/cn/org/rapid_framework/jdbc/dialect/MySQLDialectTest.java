package cn.org.rapid_framework.jdbc.dialect;

import junit.framework.Assert;

import org.junit.Test;


public class MySQLDialectTest {

	Dialect dialect = new MySQLDialect();
	@Test
	public void getLimitString() {
		Assert.assertEquals("select * from user limit 0", dialect.getLimitString("select * from user", 0, 0));
		Assert.assertEquals("select * from user limit 12", dialect.getLimitString("select * from user", 0, 12));
		Assert.assertEquals("select * from user limit 12,0", dialect.getLimitString("select * from user", 12, 0));
		Assert.assertEquals("select * from user limit 12,34", dialect.getLimitString("select * from user", 12, 34));		
	}
	
}
