package cn.org.rapid_framework.generator.provider.db.table.model;

import java.util.LinkedHashSet;

/**
 * 包含持有一组 Table对象
 * @author badqiu
 *
 */
public class TableSet implements java.io.Serializable{
	private static final long serialVersionUID = -6500047411657968878L;
	
	private LinkedHashSet<Table> tables = new LinkedHashSet<Table>();

	public LinkedHashSet<Table> getTables() {
		return tables;
	}

	public void setTables(LinkedHashSet<Table> columns) {
		this.tables = columns;
	}
	
	public void addTable(Table c) {
		tables.add(c);
	}

	public Table getBySqlName(String name) {
		for(Table c : tables) {
			if(name.equalsIgnoreCase(c.getSqlName())) {
				return c;
			}
		}
		return null;
	}
	
	public Table getByClassName(String name) {
		for(Table c : tables) {
			if(name.equals(c.getClassName())) {
				return c;
			}
		}
		return null;
	}
	
}
