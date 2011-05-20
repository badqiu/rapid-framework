package cn.org.rapid_framework.jdbc.dialect;

import static junit.framework.Assert.*;

import org.junit.Test;


public class MySQLDialectTest {

	Dialect dialect = new MySQLDialect();
	
	@Test
	public void getLimitString() {
		assertEquals("select * from user limit 0", dialect.getLimitString("select * from user", 0, 0));
		assertEquals("select * from user limit 12", dialect.getLimitString("select * from user", 0, 12));
		assertEquals("select * from user limit 12,0", dialect.getLimitString("select * from user", 12, 0));
		assertEquals("select * from user limit 12,34", dialect.getLimitString("select * from user", 12, 34));		
		
	}
	
	@Test
	public void getLimitStringWithPlaceHolader2() {
		assertEquals("select * from user limit #{limit}", dialect.getLimitString("select * from user", 0,"#{offset}", 0,"#{limit}"));
		assertEquals("select * from user limit #{limit}", dialect.getLimitString("select * from user", 0,"#{offset}",12,"#{limit}"));
		assertEquals("select * from user limit #{offset},#{limit}", dialect.getLimitString("select * from user", 12, "#{offset}",0,"#{limit}"));
		assertEquals("select * from user limit #{offset},#{limit}", dialect.getLimitString("select * from user", 12,"#{offset}", 34,"#{limit}"));
	}

	
	@Test
	public void getLimitString2() {
		assertEquals("select * from user limit 12", dialect.getLimitString("select * from user", 0, 12));
		assertEquals("select * from user limit 12,34", dialect.getLimitString("select * from user", 12, 34));		
		
	}
	
	@Test
	public void getLimitStringWithPlaceHolader3() {
		assertEquals("select * from user limit #{limit}", dialect.getLimitString("select * from user", 0,"#{offset}", 0,"#{limit}"));
		assertEquals("select * from user limit #{offset},#{limit}", dialect.getLimitString("select * from user", 12, "#{offset}",0,"#{limit}"));
	}
	
	@Test
	public void getLimitStringWithPlaceHolader() {
		String OFFSET = ":offset";
		String LIMIT = ":limit";
		assertEquals("select * from user limit :limit", dialect.getLimitString("select * from user", 0,OFFSET, 0,LIMIT));
		assertEquals("select * from user limit :limit", dialect.getLimitString("select * from user", 0,OFFSET,12,LIMIT));
		assertEquals("select * from user limit :offset,:limit", dialect.getLimitString("select * from user", 12, OFFSET,0,LIMIT));
		assertEquals("select * from user limit :offset,:limit", dialect.getLimitString("select * from user", 12,OFFSET, 34,LIMIT));
	}
}
