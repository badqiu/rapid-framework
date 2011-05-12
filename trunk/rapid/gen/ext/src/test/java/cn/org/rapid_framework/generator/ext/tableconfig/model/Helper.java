package cn.org.rapid_framework.generator.ext.tableconfig.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cn.org.rapid_framework.generator.Generator;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.StringHelper;

public class Helper {
	
	public static Map getMapBySql(TableConfig t, String operation) {
		Sql sql = getSql(t,operation);
    	Map map = newMapFromSql(sql,t);
		return map;
	}
    
    public static Map newMapFromSql(Sql sql,TableConfig tableConfig) {
    	Map map = new HashMap();
    	map.putAll(BeanHelper.describe(sql));
    	map.put("sql", sql);
    	map.put("tableConfig", tableConfig);
    	map.put("basepackage", tableConfig.getBasepackage());
    	return map;
    }

    public static Map newMapFromTableConfigSet(TableConfigSet tableConfigSet) {
    	Map map = new HashMap();
    	map.putAll(BeanHelper.describe(tableConfigSet));
    	map.put("tableConfigSet", tableConfigSet);
    	map.put("StringHelper", new StringHelper());
    	return map;
    }

    public static Map newMapFromTableConfig(TableConfig tableConfig) {
    	Map map = new HashMap();
    	String[] ignoreProperties = new String[]{"sqls"};
        map.putAll(BeanHelper.describe(tableConfig,ignoreProperties));
        map.put("tableConfig", tableConfig);
        map.put("StringHelper", new StringHelper());
        
        map.put("appName", "badqiu_test_app");
        map.put("appModule", "badqiu_test_module");
        map.put("databaseType", "mysql");
        return map;
    }
    
    private static Sql getSql(TableConfig t, String name) {
		for(Sql sql : t.getSqls()) {
			if(sql.getOperation().equals(name)) {
				return sql;
			}
		}
		throw new IllegalArgumentException("not found sql with name:"+name);
	}

}