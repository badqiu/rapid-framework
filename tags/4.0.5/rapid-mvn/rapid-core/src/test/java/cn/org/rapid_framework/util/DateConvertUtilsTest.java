package cn.org.rapid_framework.util;

import java.sql.Timestamp;

import junit.framework.TestCase;
/**
 * @author badqiu
 */
public class DateConvertUtilsTest extends TestCase {

	public void testConvertString2Date() {
		java.util.Date d = DateConvertUtils.parse("1999-09-09","yyyy-MM-dd",java.util.Date.class);
		Timestamp t = DateConvertUtils.parse("1999-09-09","yyyy-MM-dd",Timestamp.class);
		java.sql.Date sd = DateConvertUtils.parse("1999-09-09","yyyy-MM-dd",java.sql.Date.class);
		java.sql.Time st = DateConvertUtils.parse("1999-09-09","yyyy-MM-dd",java.sql.Time.class);
	}
	
}
