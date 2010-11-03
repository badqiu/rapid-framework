package cn.org.rapid_framework.generator.provider.db.table.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
/**
 * 包含持有一组 Column对象
 * @author badqiu
 *
 */
public class ColumnSet {
	private LinkedHashSet<Column> columns = new LinkedHashSet<Column>();

	public LinkedHashSet<Column> getColumns() {
		return columns;
	}

	public void setColumns(LinkedHashSet<Column> columns) {
		this.columns = columns;
	}
	
	public void addColumn(Column c) {
		columns.add(c);
	}

	public Column getBySqlName(String name) {
		for(Column c : columns) {
			if(name.equalsIgnoreCase(c.getSqlName())) {
				return c;
			}
		}
		return null;
	}
	
	public Column getByColumnName(String name) {
		for(Column c : columns) {
			if(name.equals(c.getColumnName())) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * 得到是主键的全部column
	 * @return
	 */	
	public List<Column> getPkColumns() {
		List results = new ArrayList();
		for(Column c : getColumns()) {
			if(c.isPk())
				results.add(c);
		}
		return results;
	}
	
	/**
	 * 得到不是主键的全部column
	 * @return
	 */
	public List<Column> getNotPkColumns() {
		List results = new ArrayList();
		for(Column c : getColumns()) {
			if(!c.isPk())
				results.add(c);
		}
		return results;
	}
	
	/**
	 * 得到主键总数
	 * @return
	 */
	public int getPkCount() {
		int pkCount = 0;
		for(Column c : columns){
			if(c.isPk()) {
				pkCount ++;
			}
		}
		return pkCount;
	}
	
	/** 得到单主键，等价于getPkColumns().get(0)  */
	public Column getPkColumn() {
		if(getPkColumns().isEmpty()) {
			return null;
		}
		return getPkColumns().get(0);
	}
}
