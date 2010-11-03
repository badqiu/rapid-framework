package cn.org.rapid_framework.generator.provider.db.table.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class TableSet {
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
