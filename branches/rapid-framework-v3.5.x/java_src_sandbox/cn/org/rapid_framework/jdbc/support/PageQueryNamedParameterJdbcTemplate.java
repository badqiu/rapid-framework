package cn.org.rapid_framework.jdbc.support;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import cn.org.rapid_framework.jdbc.dialect.Dialect;

public class PageQueryNamedParameterJdbcTemplate {
	
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	Dialect dialect;
	int limit;
	int offset;
	String sql;
	RowMapper rowMapper;
	Map offsetLimitParamMap = new HashMap();
	
	public List pageQuery(final Map paramMap) {
		toLimitSql();
		paramMap.putAll(offsetLimitParamMap);
		return (List)namedParameterJdbcTemplate.query(sql, paramMap, new OffsetLimitResultSetExtractor(offset,limit,rowMapper));
	}
	
	public List pageQuery(final SqlParameterSource paramSource) {
		toLimitSql();
		
		SqlParameterSource delegateParamSource = new SqlParameterSource() {
			public int getSqlType(String paramName) {
				return paramSource.getSqlType(paramName);
			}

			public String getTypeName(String paramName) {
				return paramSource.getTypeName(paramName);
			}

			public Object getValue(String paramName) throws IllegalArgumentException {
				if(isOffsetLimitParam(paramName)) {
					return offsetLimitParamMap.get(paramName);
				}else {
					return paramSource.getValue(paramName);
				}
			}

			public boolean hasValue(String paramName) {
				if(isOffsetLimitParam(paramName)) {
					return offsetLimitParamMap.containsKey(paramName);
				}else {
					return paramSource.hasValue(paramName);
				}
			}
			private boolean isOffsetLimitParam(String paramName) {
				if(LIMIT_PLACEHOLDER.substring(1).equals(paramName)) {
					return true;
				}
				if(OFFSET_PLACEHOLDER.substring(1).equals(paramName)) {
					return true;
				}
				return false;
			}
		};
		
		return (List)namedParameterJdbcTemplate.query(sql,delegateParamSource, new OffsetLimitResultSetExtractor(offset,limit,rowMapper));
	}
	
	static final String LIMIT_PLACEHOLDER = ":__limit";
	static final String OFFSET_PLACEHOLDER = ":__offset";
	private void toLimitSql() {
		//支持limit查询
		if(dialect.supportsLimit()) {
			offsetLimitParamMap.put(LIMIT_PLACEHOLDER.substring(1), limit);
			
			//支持limit及offset.则完全使用数据库分页
			if(dialect.supportsLimitOffset()) {
				offsetLimitParamMap.put(OFFSET_PLACEHOLDER.substring(1), offset);
				sql = dialect.getLimitString(sql,offset,OFFSET_PLACEHOLDER,limit,LIMIT_PLACEHOLDER);
				offset = 0;
			}else {
				//不支持offset,则在后面查询中使用游标配合limit分页
				sql = dialect.getLimitString(sql, 0,null, limit,LIMIT_PLACEHOLDER);
			}
			
			limit = Integer.MAX_VALUE;
		}
	}
	
}
