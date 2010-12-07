package cn.org.rapid_framework.generator.provider.db.table;

import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public interface TableFactoryListener {
	
	public void onTableCreated(Table t);
	
}