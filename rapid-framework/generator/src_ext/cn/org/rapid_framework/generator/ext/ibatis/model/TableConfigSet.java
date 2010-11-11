package cn.org.rapid_framework.generator.ext.ibatis.model;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.org.rapid_framework.generator.util.StringHelper;

public class TableConfigSet implements Iterable<TableConfig>{
	private Set<TableConfig> tableConfigs = new LinkedHashSet<TableConfig>();
	
	public Set<String> getSequences() {
		Set<String> result = new LinkedHashSet<String>();
		for(TableConfig c : tableConfigs) {
		    if(StringHelper.isNotBlank(c.getSequence())) {
		        result.add(c.getSequence());
		    }
		}
		return result;
	}
	
	public void addTableConfig(TableConfig t) {
		tableConfigs.add(t);
	}
	
	public Set<TableConfig> getTableConfigs() {
		return tableConfigs;
	}

	public void setTableConfigs(Set<TableConfig> tableConfigs) {
		this.tableConfigs = tableConfigs;
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

	public Iterator<TableConfig> iterator() {
		return tableConfigs.iterator();
	}
}
