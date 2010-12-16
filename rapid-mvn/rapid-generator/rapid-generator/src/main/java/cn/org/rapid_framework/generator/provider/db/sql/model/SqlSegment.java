package cn.org.rapid_framework.generator.provider.db.sql.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.sqlparse.NamedParameterUtils;
import cn.org.rapid_framework.generator.util.sqlparse.ParsedSql;

/**
 *  SQL片段, 代表一句SQL include 其它的sql片段. 如ibatis中的 <include refid='User.Where'/> 
 *  
 *  @see Sql
 **/
public class SqlSegment {
	/** sql segment ID */
	public String id;
	/** 原始的 被 include的一段 SQL */
	public String rawIncludeSql;
	/** 已经经过处理解析的一段SQL */
	public String parsedIncludeSql;
	/** 这段include sql包含的参数列表 */
	public Set<SqlParameter> params;
	
	//TODO 增加如果参数数是1,则不生成  SqlSegemnt,此处也要修改对1的特殊控制
	public Set<SqlParameter> getParams(Sql sql) {
		Set<SqlParameter> result = new LinkedHashSet();
		for(String paramName : getParamNames()) {
			SqlParameter p = sql.getParam(paramName);
			if(p == null) throw new IllegalArgumentException("not found param on sql:"+parsedIncludeSql+" with name:"+paramName+" for sqlSegment:"+id); //是否不该扔异常
			result.add(p);
		}
		return result;
	}
	public List<String> getParamNames() {
		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(parsedIncludeSql);
		return parsedSql.getParameterNames();
	}
	public String getClassName() {
		return StringHelper.toJavaClassName(id.replace(".", "_").replace("-", "_"));
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRawIncludeSql() {
		return rawIncludeSql;
	}
	public void setRawIncludeSql(String rawIncludeSql) {
		this.rawIncludeSql = rawIncludeSql;
	}
	public String getParsedIncludeSql() {
		return parsedIncludeSql;
	}
	public void setParsedIncludeSql(String parsedIncludeSql) {
		this.parsedIncludeSql = parsedIncludeSql;
	}
	public Set<SqlParameter> getParams() {
		return params;
	}
	public void setParams(Set<SqlParameter> params) {
		this.params = params;
	}
	
}
