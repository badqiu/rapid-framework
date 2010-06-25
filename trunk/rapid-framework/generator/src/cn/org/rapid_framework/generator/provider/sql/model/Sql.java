package cn.org.rapid_framework.generator.provider.sql.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import cn.org.rapid_framework.generator.provider.db.model.Column;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.util.StringHelper;

public  class Sql {
    	String operation = null;
    	String multiPolicy = "many"; // many or one
    	Set<Column> columns = new LinkedHashSet<Column>();
    	String queryResultClassName = null;
    	List<SqlParameter> params = new ArrayList();
    	
    	String sourceSql; // source sql
//    	String jdbcSql; // jdbc sql
//    	String ibatisSql; //ibatis sql
//    	String hql; //hibernate sql
//    	String ibatis3Sql; //ibatis3 sql
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
    	public String getQueryResultClassName() {
    		if(columns.size() == 1) 
    			throw new IllegalArgumentException("only single column by query,cannot execute method:getQueryResultClassName(), operation:"+operation);
    		if(queryResultClassName != null) return queryResultClassName;
    		if(isInSameTable()) {
    			return columns.iterator().next().getTable().getClassName();
    		}else {
    			if(operation == null) return null;
    			return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(operation))+"Result";
    		}
		}
		public void setQueryResultClassName(String queryResultClassName) {
			this.queryResultClassName = queryResultClassName;
		}
		//TODO columnsSize大于二并且不是在同一张表中,将创建一个QueryResultClassName类,同一张表中也要考虑创建类
		public int getColumnsSize() {
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
//			setJdbcSql(sourceSql);
//			setHql(sourceSql);
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
			return replaceParamsWith("#","#");
		}
		public String getHql() {
			return sourceSql;
		}
		public String getIbatis3Sql() {
			return replaceParamsWith("#{","}");
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
    }