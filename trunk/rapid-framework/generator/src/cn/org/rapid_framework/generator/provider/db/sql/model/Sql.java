package cn.org.rapid_framework.generator.provider.db.sql.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;

public class Sql {
	public static String MULTI_POLICY_ONE = "one";
	public static String MULTI_POLICY_MANY = "many";
	
	String tableSqlName = null; //是否需要
	String operation = null;
	String resultClass;
	String parameterClass;
	String remarks;
	
	String multiPolicy = "many"; // many or one
	
	LinkedHashSet<Column> columns = new LinkedHashSet<Column>();
	LinkedHashSet<SqlParameter> params = new LinkedHashSet<SqlParameter>();
	
	String sourceSql; // source sql
	String executeSql;
	
	public Sql() {
	}
	
	public Sql(Sql sql) {
		this.tableSqlName = sql.tableSqlName;
		
		this.operation = sql.operation;
		this.parameterClass = sql.parameterClass;
		this.resultClass = sql.resultClass;
		this.multiPolicy = sql.multiPolicy;
		
		this.columns = sql.columns;
		this.params = sql.params;
		this.sourceSql = sql.sourceSql;
		this.executeSql = sql.executeSql;
		this.remarks = sql.remarks;
	}
	
	public boolean isColumnsInSameTable() {
		//FIXME 还要增加表的列数与columns是否相等,才可以为select 生成 include语句
		if(columns == null || columns.isEmpty()) return false;
		if(columns.size() == 1 && columns.iterator().next().getTable() != null) return true;
		String preTableName = columns.iterator().next().getSqlName();
		for(Column c :columns) {
			Table table = c.getTable();
			if(table == null) {
				return false;
			}
			if(preTableName.equalsIgnoreCase(table.getSqlName())) {
				continue;
			}
		}
		return true;
	}
	public String getResultClass() {
		if(StringHelper.isNotBlank(resultClass)) return resultClass;
		if(columns.size() == 1) {
			return columns.iterator().next().getSimpleJavaType();
		}
		if(isColumnsInSameTable()) {
			return columns.iterator().next().getTable().getClassName();
		}else {
			if(operation == null) return null;
			return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(operation))+"Result";
		}
	}    
	public void setResultClass(String queryResultClass) {
		this.resultClass = queryResultClass;
	}
	public String getResultClassName() {
		int lastIndexOf = getResultClass().lastIndexOf(".");
		return lastIndexOf >= 0 ? getResultClass().substring(lastIndexOf+1) : getResultClass();
	}
	public String getParameterClass() {
		if(StringHelper.isNotBlank(parameterClass)) return parameterClass;
		if(StringHelper.isBlank(operation)) return null;
		if(isSelectSql()) {
			return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(operation))+"Query";
		}else {
			return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(operation))+"Parameter";
		}
	}
	public void setParameterClass(String operationParameterClass) {
		this.parameterClass = operationParameterClass;
	}
	public String getParameterClassName() {
		int lastIndexOf = getParameterClass().lastIndexOf(".");
		return lastIndexOf >= 0 ? getParameterClass().substring(lastIndexOf+1) : getParameterClass();
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
	public LinkedHashSet<Column> getColumns() {
		return columns;
	}
	public void setColumns(LinkedHashSet<Column> columns) {
		this.columns = columns;
	}
	public LinkedHashSet<SqlParameter> getParams() {
		return params;
	}
	public void setParams(LinkedHashSet<SqlParameter> params) {
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
	
	public String getExecuteSql() {
		return executeSql;
	}
	
	public void setExecuteSql(String executeSql) {
		this.executeSql = executeSql;
	}
	
	public String getJdbcSql() {
		return sourceSql;
	}
	
	public String getIbatisSql() {
		String sql = replaceParamsWith("#","#");
		if(isSelectSql() && SqlParseHelper.getSelect(sourceSql).indexOf("*") >= 0) {
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
		if(isSelectSql() && SqlParseHelper.getSelect(sourceSql).indexOf("*") >= 0) {
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
	
	public String getTableSqlName() {
		return tableSqlName;
	}

	public void setTableSqlName(String tableName) {
		this.tableSqlName = tableName;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String comments) {
		this.remarks = comments;
	}

	public String getTableClassName() {
		if(StringHelper.isBlank(tableSqlName)) return null;
		return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(tableSqlName));
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