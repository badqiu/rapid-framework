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
	
	String className = null;
	String operation = null;
	String operationResultClass;
	String operationParameterClass;
	
	String multiPolicy = "many"; // many or one
	
	LinkedHashSet<Column> columns = new LinkedHashSet<Column>();
	LinkedHashSet<SqlParameter> params = new LinkedHashSet<SqlParameter>();
	
	String sourceSql; // source sql
	String executeSql;
	
	public boolean isColumnsInSameTable() {
		//FIXME 还要增加表的列数与columns是否相等,才可以为select 生成 include语句
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
	public String getOperationResultClass() {
		if(StringHelper.isNotBlank(operationResultClass)) return operationResultClass;
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
	public void setOperationResultClass(String queryResultClass) {
		this.operationResultClass = queryResultClass;
	}
	public String getOperationResultClassName() {
		return getOperationResultClass();
	}
	public String getOperationParameterClass() {
		if(operationParameterClass != null) return operationParameterClass;
		if(isSelectSql()) {
			return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(operation))+"Query";
		}else {
			return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(operation))+"Parameter";
		}
	}
	public void setOperationParameterClass(String operationParameterClass) {
		this.operationParameterClass = operationParameterClass;
	}
	public String getOperationParameterClassName() {
		return getOperationParameterClass();
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