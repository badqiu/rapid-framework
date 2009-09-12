package cn.org.rapid_framework.jdbc.dialect;

public class DerbyDialect implements Dialect{

	public String getLimitString(String sql, int offset, int limit) {
		throw new UnsupportedOperationException( "paged queries not supported" );
	}

	public boolean supportsLimit() {
		return false;
	}

	public boolean supportsLimitOffset() {
		return false;
	}

}
