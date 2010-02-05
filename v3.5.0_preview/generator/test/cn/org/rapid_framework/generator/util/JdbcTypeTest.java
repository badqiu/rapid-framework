package cn.org.rapid_framework.generator.util;

import java.sql.Types;

import org.junit.Test;

import static org.junit.Assert.*;

public class JdbcTypeTest {
	@Test
	public void test() {
		assertEquals("BIT",JdbcType.getJdbcSqlTypeName(Types.BIT));
		assertEquals("BLOB",JdbcType.getJdbcSqlTypeName(Types.BLOB));
		assertEquals("BIGINT",JdbcType.getJdbcSqlTypeName(Types.BIGINT));
	}
}
