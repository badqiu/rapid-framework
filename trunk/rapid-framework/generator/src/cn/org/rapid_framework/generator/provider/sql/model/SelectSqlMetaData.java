package cn.org.rapid_framework.generator.provider.sql.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.org.rapid_framework.generator.provider.db.model.Column;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.util.StringHelper;

public  class SelectSqlMetaData {
    	String operation = null;
    	String multiPolicy = "many"; // many or one
    	Set<Column> columns = new LinkedHashSet<Column>();
    	String queryResultClassName = null;
    	List<SelectParameter> params = new ArrayList();
    	
    	String sourceSql; // source sql
    	String jdbcSql; // jdbc sql
    	String ibatisSql; //ibatis sql
    	String hql; //hibernate sql
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
		public List<SelectParameter> getParams() {
			return params;
		}
		public void setParams(List<SelectParameter> params) {
			this.params = params;
		}
		
		public String getSourceSql() {
			return sourceSql;
		}
		public void setSourceSql(String sourceSql) {
			this.sourceSql = sourceSql;
			setJdbcSql(sourceSql);
			setHql(sourceSql);
			setIbatisSql(sourceSql);
		}
		public String getJdbcSql() {
			return jdbcSql;
		}
		public void setJdbcSql(String jdbcSql) {
			this.jdbcSql = jdbcSql;
		}
		public String getIbatisSql() {
			return ibatisSql;
		}
		public void setIbatisSql(String ibatisSql) {
			this.ibatisSql = ibatisSql;
		}
		public String getHql() {
			return hql;
		}
		public void setHql(String hql) {
			this.hql = hql;
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