package cn.org.rapid_framework.jdbc.sqlgenerator.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
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
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public List<Column> getPrimaryKeyColumns() {
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
