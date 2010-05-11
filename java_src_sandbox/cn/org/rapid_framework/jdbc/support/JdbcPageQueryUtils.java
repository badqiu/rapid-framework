package cn.org.rapid_framework.jdbc.support;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import cn.org.rapid_framework.jdbc.dialect.Dialect;

public class JdbcPageQueryUtils {

	public static List pageQuery(NamedParameterJdbcTemplate jdbcTemplate,Dialect dialect,SqlOffsetLimit sqlOffsetLimit, final Map paramMap,final RowMapper rowMapper) {
		sqlOffsetLimit = toLimitSql(dialect, sqlOffsetLimit, paramMap);
		return (List)jdbcTemplate.query(sqlOffsetLimit.sql, paramMap, new OffsetLimitResultSetExtractor(sqlOffsetLimit.offset,sqlOffsetLimit.limit,rowMapper));
	}
	
	static final String LIMIT_PLACEHOLDER = ":__limit";
	static final String OFFSET_PLACEHOLDER = ":__offset";
	private static SqlOffsetLimit toLimitSql(Dialect dialect,SqlOffsetLimit sqlOffsetLimit, final Map paramMap) {
		int limit = sqlOffsetLimit.limit;
		int offset = sqlOffsetLimit.offset;
		String sql = sqlOffsetLimit.sql;
		//支持limit查询
		if(dialect.supportsLimit()) {
			paramMap.put(LIMIT_PLACEHOLDER.substring(1), limit);
			
			//支持limit及offset.则完全使用数据库分页
			if(dialect.supportsLimitOffset()) {
				paramMap.put(OFFSET_PLACEHOLDER.substring(1), offset);
				sql = dialect.getLimitString(sql,offset,OFFSET_PLACEHOLDER,limit,LIMIT_PLACEHOLDER);
				offset = 0;
			}else {
				//不支持offset,则在后面查询中使用游标配合limit分页
				sql = dialect.getLimitString(sql, 0,null, limit,LIMIT_PLACEHOLDER);
			}
			
			limit = Integer.MAX_VALUE;
		}
		
		return new SqlOffsetLimit(sql,offset,limit);
	}
	
	public static List pageQuery(JdbcTemplate jdbcTemplate,Dialect dialect,SqlOffsetLimit sqlOffsetLimit, PreparedStatementSetter pss,final RowMapper rowMapper) {
		sqlOffsetLimit = toLimitSql(dialect, sqlOffsetLimit);
		return (List)jdbcTemplate.query(sqlOffsetLimit.sql,pss, new OffsetLimitResultSetExtractor(sqlOffsetLimit.offset,sqlOffsetLimit.limit,rowMapper));
	}
	
	private static SqlOffsetLimit toLimitSql(Dialect dialect,SqlOffsetLimit sqlOffsetLimit) {
		int limit = sqlOffsetLimit.limit;
		int offset = sqlOffsetLimit.offset;
		String sql = sqlOffsetLimit.sql;
		//支持limit查询
		if(dialect.supportsLimit()) {
			//支持limit及offset.则完全使用数据库分页
			if(dialect.supportsLimitOffset()) {
				sql = dialect.getLimitString(sql,offset,limit);
				offset = 0;
			}else {
				//不支持offset,则在后面查询中使用游标配合limit分页
				sql = dialect.getLimitString(sql, 0, limit);
			}
			
			limit = Integer.MAX_VALUE;
		}
		
		return new SqlOffsetLimit(sql,offset,limit);
	}
	
}
