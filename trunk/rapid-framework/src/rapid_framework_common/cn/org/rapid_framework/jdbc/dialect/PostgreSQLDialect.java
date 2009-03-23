package cn.org.rapid_framework.jdbc.dialect;
/**
 * @author badqiu
 */
public class PostgreSQLDialect implements Dialect{
	
	public boolean supportsLimit() {
		return true;
	}

	public boolean supportsLimitOffset(){
		return true;
	}
	
	public String getLimitString(String sql, int offset, int limit) {
		return new StringBuffer( sql.length()+20 )
		.append(sql)
		.append(offset > 0 ? " limit "+limit+" offset "+offset : " limit "+limit)
		.toString();
	}
}
