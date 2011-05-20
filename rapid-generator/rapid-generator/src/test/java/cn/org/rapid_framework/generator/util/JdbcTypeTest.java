package cn.org.rapid_framework.generator.util;


import java.sql.Types;

import cn.org.rapid_framework.generator.util.typemapping.JdbcType;

import junit.framework.TestCase;


public class JdbcTypeTest extends TestCase {
	public void test() {
		assertEquals("BIT",JdbcType.getJdbcSqlTypeName(Types.BIT));
		assertEquals("BLOB",JdbcType.getJdbcSqlTypeName(Types.BLOB));
		assertEquals("BIGINT",JdbcType.getJdbcSqlTypeName(Types.BIGINT));
	}
}
