package cn.org.rapid_framework.generator.ext.ant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public class TableGenTask extends BaseGeneratorTask {
    private String tableSqlName; 

    @Override
    protected List<Map> getGeneratorContexts() {
        Table table = TableFactory.getInstance().getTable(tableSqlName);
        Map map = new HashMap();
        map.put("table", table);
        
        return Arrays.asList(map);
    }

    public String getTableSqlName() {
        return tableSqlName;
    }

    public void setTableSqlName(String tableSqlName) {
        this.tableSqlName = tableSqlName;
    }

    
}
