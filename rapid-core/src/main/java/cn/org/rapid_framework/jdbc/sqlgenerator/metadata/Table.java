package cn.org.rapid_framework.jdbc.sqlgenerator.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * table为metadata类,根据该类的数据生成增删改查sql
 * @author badqiu
 *
 */
public class Table {

	private String tableName;
	private List<Column> columns;

	public Table(String tableName, Column... columns) {
		this(tableName,Arrays.asList(columns));
	}

	public Table(String tableName, List<Column> columns) {
		this.setTableName(tableName);
		this.setColumns(columns);
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
		this.primaryKeyColumns = null;
	}

	public List<Column> getColumns() {
		return Collections.unmodifiableList(columns);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}
	
	List<Column> primaryKeyColumns = null;
	public List<Column> getPrimaryKeyColumns() {
		if(primaryKeyColumns == null) {
			primaryKeyColumns = getPrimaryKeyColumns0();
		}
		return primaryKeyColumns;
	}
	
	public int getPrimaryKeyCount() {
		return getPrimaryKeyColumns().size();
	}
	
	public Column getColumnBySqlName(String sqlName) {
		for(Column c : columns) {
			if(c.getSqlName().equals(sqlName)) {
				return c;
			}
		}
		return null;
	}

	public Column getColumnByPropertyName(String propertyName) {
		for(Column c : columns) {
			if(c.getPropertyName().equals(propertyName)) {
				return c;
			}
		}
		return null;
	}
	
	private List<Column> getPrimaryKeyColumns0() {
		List result = new ArrayList();
		for(Column c : getColumns()) {
			if(c.isPrimaryKey())
				result.add(c);
		}
		return result;
	}

	public String toString() {
		return "tableName:"+getTableName()+" columns:"+getColumns();
	}

}
