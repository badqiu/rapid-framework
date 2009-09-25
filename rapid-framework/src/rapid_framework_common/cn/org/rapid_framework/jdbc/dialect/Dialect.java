package cn.org.rapid_framework.jdbc.dialect;
/**
 * 类似hibernate的Dialect,但只精简出分页部分
 * @author badqiu
 */
public abstract class Dialect {
	
    public abstract boolean supportsLimit();

    public String getLimitString(String sql, int offset, int limit) {
    	return getLimitString(sql,offset,String.valueOf(offset),limit,String.valueOf(limit));
    }
 
    public abstract boolean supportsLimitOffset();
    
    public abstract String getLimitString(String sql, int offset,String offsetPlaceholder, int limit,String limitPlaceholder);
    
}
