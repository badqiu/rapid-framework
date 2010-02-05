package cn.org.rapid_framework.jdbc.dialect;

import junit.framework.Assert;

import org.junit.Test;


public class DB2DialectTest {
	
	Dialect dialect = new DB2Dialect();
	@Test
	public void getLimitString() {
		Assert.assertEquals("select * from ( select rownumber() over() as rownumber_, * from user ) as temp_ where rownumber_ <= 0", dialect.getLimitString("select * from user", 0, 0));
		Assert.assertEquals("select * from ( select rownumber() over() as rownumber_, * from user ) as temp_ where rownumber_ <= 12", dialect.getLimitString("select * from user", 0, 12));
		Assert.assertEquals("select * from ( select rownumber() over() as rownumber_, * from user ) as temp_ where rownumber_ between 12+1 and 12+0", dialect.getLimitString("select * from user", 12, 0));
		Assert.assertEquals("select * from ( select rownumber() over() as rownumber_, * from user ) as temp_ where rownumber_ between 12+1 and 12+34", dialect.getLimitString("select * from user", 12, 34));
	}
	@Test
	public void getLimitStringWithPlaceHolader() {
		String OFFSET = ":offset";
		String LIMIT = ":limit";
		Assert.assertEquals("select * from ( select rownumber() over() as rownumber_, * from user ) as temp_ where rownumber_ <= :limit", dialect.getLimitString("select * from user", 0,OFFSET, 0,LIMIT));
		Assert.assertEquals("select * from ( select rownumber() over() as rownumber_, * from user ) as temp_ where rownumber_ <= :limit", dialect.getLimitString("select * from user", 0,OFFSET,12,LIMIT));
		Assert.assertEquals("select * from ( select rownumber() over() as rownumber_, * from user ) as temp_ where rownumber_ between :offset+1 and :offset+:limit", dialect.getLimitString("select * from user", 12, OFFSET,0,LIMIT));
		Assert.assertEquals("select * from ( select rownumber() over() as rownumber_, * from user ) as temp_ where rownumber_ between :offset+1 and :offset+:limit", dialect.getLimitString("select * from user", 12,OFFSET, 34,LIMIT));
	}
}
