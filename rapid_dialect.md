
# 介绍 #
rapid中的dialect主要目的是用于分页，代码参考自hiberante,但只精简出分页部分.
适用于jdbc或是iBatis.

## 分页Dialect接口 ##

```
public abstract class Dialect {
	
    public abstract boolean supportsLimit();

    public abstract boolean supportsLimitOffset();
    
    public String getLimitString(String sql, int offset, int limit) {
    	return getLimitString(sql,offset,String.valueOf(offset),limit,String.valueOf(limit));
    }

    public abstract String getLimitString(String sql, int offset,String offsetPlaceholder, int limit,String limitPlaceholder);
    
}
```

## Mysql的Dialect实现 ##
```
public class MySQLDialect extends Dialect{

	public boolean supportsLimitOffset(){
		return true;
	}
	
        public boolean supportsLimit() {   
                return true;   
        }  
    
	public String getLimitString(String sql, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
             if (offset > 0) {   
        	return sql + " limit "+offsetPlaceholder+","+limitPlaceholder; 
             } else {   
                return sql + " limit "+limitPlaceholder;
             }  
	}   
  
}
```

## Jdbc的分页实现 ##
```
// spring jdbc现在的实现是使用占位符的方式
static final String LIMIT_PLACEHOLDER = ":__limit";
static final String OFFSET_PLACEHOLDER = ":__offset";
public List pageQuery(String sql, final Map paramMap, int startRow,int pageSize, final RowMapper rowMapper) {
	//支持limit查询
	if(dialect.supportsLimit()) {
		paramMap.put(LIMIT_PLACEHOLDER.substring(1), pageSize);
		
		//支持limit及offset.则完全使用数据库分页
		if(dialect.supportsLimitOffset()) {
			paramMap.put(OFFSET_PLACEHOLDER.substring(1), startRow);
			sql = dialect.getLimitString(sql,startRow,OFFSET_PLACEHOLDER,pageSize,LIMIT_PLACEHOLDER);
			startRow = 0;
		}else {
			//不支持offset,则在后面查询中使用游标配合limit分页
			sql = dialect.getLimitString(sql, 0,null, pageSize,LIMIT_PLACEHOLDER);
		}
		
		pageSize = Integer.MAX_VALUE;
	}
	return (List)getNamedParameterJdbcTemplate().query(sql, paramMap, new OffsetLimitResultSetExtractor(startRow,pageSize,rowMapper));
}
```

# 存在的问题 #
现ibatis2.3,ibatis3的分页参数没有使用疑问号 **占位符"?"** ,所以在如oracle这种数据库,会影响性能.


spring\_jdbc现在的实现没有这个问题