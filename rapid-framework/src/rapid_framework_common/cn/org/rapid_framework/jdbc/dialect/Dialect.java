package cn.org.rapid_framework.jdbc.dialect;
/**
 * 类似hibernate的Dialect,但只精简出分页部分
 * @author badqiu
 */
public interface Dialect {
	
    public boolean supportsLimit();

    public String getLimitString(String sql, int offset, int limit);
 
    public boolean supportsLimitOffset();
    
    //TODO Dialect.getLimitString(String sql, int offset,String offsetPlaceholder, int limit,String limitPlaceholder);
}
