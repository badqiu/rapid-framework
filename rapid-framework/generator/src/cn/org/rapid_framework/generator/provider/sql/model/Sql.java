package cn.org.rapid_framework.generator.provider.sql.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.org.rapid_framework.generator.provider.db.model.Column;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;

public class Sql {
	public static String MULTI_POLICY_ONE = "one";
	public static String MULTI_POLICY_MANY = "many";
	
	String operation = null;
	String multiPolicy = "many"; // many or one
	Set<Column> columns = new LinkedHashSet<Column>();
	String queryResultClass;
	List<SqlParameter> params = new ArrayList();
	
	String sourceSql; // source sql
	
	public boolean isInSameTable() {
		if(columns.isEmpty()) return false;
		if(columns.size() == 1 && columns.iterator().next().getTable() != null) return true;
		String preTableName = columns.iterator().next().getSqlName();
		for(Column c :columns) {
			Table table = c.getTable();
			if(table == null) {
				return false;
			}
			if(preTableName.equals(table.getSqlName())) {
				continue;
			}
		}
		return true;
	}
	public String getQueryResultClass() {
		if(columns.size() == 1) {
//    			throw new IllegalArgumentException("only single column by query,cannot execute method:getQueryResultClassName(), operation:"+operation);
			return columns.iterator().next().getSimpleJavaType();
		}
		if(queryResultClass != null) return queryResultClass;
		if(isInSameTable()) {
			return columns.iterator().next().getTable().getClassName();
		}else {
			if(operation == null) return null;
			return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(operation))+"Result";
		}
	}    
	public void setQueryResultClass(String queryResultClass) {
		this.queryResultClass = queryResultClass;
	}
	public String getQueryResultClassName() {
		return getQueryResultClass();
	}
	//TODO columnsSize大于二并且不是在同一张表中,将创建一个QueryResultClassName类,同一张表中也要考虑创建类
	public int getColumnsCount() {
		return columns.size();
	}
	public void addColumn(Column c) {
		columns.add(c);
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getOperationFirstUpper() {
		return StringHelper.capitalize(getOperation());
	}
	public String getMultiPolicy() {
		return multiPolicy;
	}
	public void setMultiPolicy(String multiPolicy) {
		this.multiPolicy = multiPolicy;
	}
	public Set<Column> getColumns() {
		return columns;
	}
	public void setColumns(Set<Column> columns) {
		this.columns = columns;
	}
	public List<SqlParameter> getParams() {
		return params;
	}
	public void setParams(List<SqlParameter> params) {
		this.params = params;
	}
	
	public String getSourceSql() {
		return sourceSql;
	}
	public void setSourceSql(String sourceSql) {
		this.sourceSql = sourceSql;
	}
	
	public String replaceParamsWith(String prefix,String suffix) {
		String sql = sourceSql;
		List<SqlParameter> sortedParams = new ArrayList(params);
		Collections.sort(sortedParams,new Comparator<SqlParameter>() {
			public int compare(SqlParameter o1, SqlParameter o2) {
				return o2.paramName.length() - o1.paramName.length();
			}
		});
		for(SqlParameter s : sortedParams){
			sql = StringHelper.replace(sql,":"+s.getParamName(),prefix+s.getParamName()+suffix);
		}
		return sql;
	}
	
	public String getJdbcSql() {
		return sourceSql;
	}
	
	public String getIbatisSql() {
		String sql = replaceParamsWith("#","#");
		if(isSelectSql()) {
			return SqlParseHelper.getPrettySql("select " + joinColumnsSqlName() + " " + SqlParseHelper.removeSelect(sql));
		}else {
			return sql;
		}
	}
	
	private String joinColumnsSqlName() {
		StringBuffer sb = new StringBuffer();
		for(Iterator<Column> it = columns.iterator();it.hasNext();) {
			sb.append(it.next().getSqlName());
			if(it.hasNext()) sb.append(",");
		}
		return sb.toString();
	}
	
	public String getHql() {
		return sourceSql;
	}
	
	public String getIbatis3Sql() {
		String sql = replaceParamsWith("#{","}");
		if(isSelectSql()) {
			return SqlParseHelper.getPrettySql("select " + joinColumnsSqlName() + " " + SqlParseHelper.removeSelect(sql));
		}else {
			return sql;
		}
	}
	
	public boolean isSelectSql() {
		return sourceSql.trim().toLowerCase().matches("(?i)\\s*select\\s.*from\\s+.*");
	}
	
	public boolean isUpdateSql() {
		return sourceSql.trim().toLowerCase().matches("(?i)\\s*update\\s+.*");
	}
	
	public boolean isDeleteSql() {
		return sourceSql.trim().toLowerCase().matches("(?i)\\s*delete\\s+from\\s.*");
	}
	
	public boolean isInsertSql() {
		return sourceSql.trim().toLowerCase().matches("(?i)\\s*insert\\s+into\\s+.*");
	}
	
	public Column getColumnBySqlName(String sqlName) {
		for(Column c : getColumns()) {
			if(c.getSqlName().equalsIgnoreCase(sqlName)) {
				return c;
			}
		}
		return null;
	}
	
	public Column getColumnByName(String name) {
	    Column c = getColumnBySqlName(name);
	    if(c == null) {
	    	c = getColumnBySqlName(StringHelper.toUnderscoreName(name));
	    }
	    return c;
	}
}