package cn.org.rapid_framework.generator.ext.ibatis.model;

import java.util.LinkedHashSet;
import java.util.Set;

public class TableConfigSet {
	private Set<TableConfig> tableConfigs = new LinkedHashSet<TableConfig>();
	
	public Set<String> getSequences() {
		Set<String> result = new LinkedHashSet<String>();
		for(TableConfig c : tableConfigs) {
			result.add(c.getSequence());
		}
		return result;
	}
	
	public void addTableConfig(TableConfig t) {
		tableConfigs.add(t);
	}
	
	public TableConfig getBySqlName(String sqlName) {
		for(TableConfig c : tableConfigs) {
			if(sqlName.equalsIgnoreCase(c.getSqlname())) {
				return c;
			}
		}
		return null;
	}
	
	public TableConfig getByClassName(String name) {
		for(TableConfig c : tableConfigs) {
			if(name.equals(c.getTableClassName())) {
				return c;
			}
		}
		return null;
	}
}
