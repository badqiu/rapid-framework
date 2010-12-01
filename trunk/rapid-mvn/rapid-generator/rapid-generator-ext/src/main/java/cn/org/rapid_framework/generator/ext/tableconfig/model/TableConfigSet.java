package cn.org.rapid_framework.generator.ext.tableconfig.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.org.rapid_framework.generator.util.StringHelper;

public class TableConfigSet implements Iterable<TableConfig>{
	private Set<TableConfig> tableConfigs = new LinkedHashSet<TableConfig>();
    
	private String _package;
    
    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
        setPackageForTableConfigs();
    }
    
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
	    t.setPackage(getPackage());
		tableConfigs.add(t);
	}
	
	public Set<TableConfig> getTableConfigs() {
		return Collections.unmodifiableSet(tableConfigs);
	}

	public void setTableConfigs(Set<TableConfig> tableConfigs) {
		this.tableConfigs = tableConfigs;
		setPackageForTableConfigs();
	}

    private void setPackageForTableConfigs() {
        for(TableConfig t : this.tableConfigs) {
		    t.setPackage(getPackage());
		}
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
			if(name.equals(c.getClassName())) {
				return c;
			}
		}
		return null;
	}

	public Iterator<TableConfig> iterator() {
		return tableConfigs.iterator();
	}
}
