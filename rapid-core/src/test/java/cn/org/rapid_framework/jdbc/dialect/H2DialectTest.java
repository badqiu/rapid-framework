package cn.org.rapid_framework.jdbc.dialect;

import junit.framework.Assert;
import junit.framework.TestCase;

public class H2DialectTest extends TestCase{

	Dialect dialect = new H2Dialect();
	public void test_getLimitString() {
		assertEquals("select * from user limit 0", dialect.getLimitString("select * from user", 0, 0));
		assertEquals("select * from user limit 12", dialect.getLimitString("select * from user", 0, 12));
		assertEquals("select * from user limit 0 offset 12", dialect.getLimitString("select * from user", 12, 0));
		assertEquals("select * from user limit 34 offset 12", dialect.getLimitString("select * from user", 12, 34));		
		
	}
	public void test_getLimitStringWithPlaceHolader() {
		String OFFSET = ":offset";
		String LIMIT = ":limit";
		assertEquals("select * from user limit :limit", dialect.getLimitString("select * from user", 0,OFFSET, 0,LIMIT));
		assertEquals("select * from user limit :limit", dialect.getLimitString("select * from user", 0,OFFSET,12,LIMIT));
		assertEquals("select * from user limit :limit offset :offset", dialect.getLimitString("select * from user", 12, OFFSET,0,LIMIT));
		assertEquals("select * from user limit :limit offset :offset", dialect.getLimitString("select * from user", 12,OFFSET, 34,LIMIT));
	}
	
}
