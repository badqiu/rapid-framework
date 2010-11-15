package cn.org.rapid_framework.generator.ext.ant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.generator.ext.config.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfig;
import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfigSet;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.StringHelper;

public class TableConfigGenTask extends BaseGeneratorTask {
	private String tableConfigFiles; 
	private String tableSqlName;
	
	@Override
    protected List<Map> getGeneratorContexts() {
        TableConfigSet tableConfigSet = parseForTableConfigSet();
        if("*".equals(tableSqlName)) {
            return toMaps(tableConfigSet.getTableConfigs());
        }else {
            if(tableConfigFiles.toLowerCase().indexOf(tableSqlName.toLowerCase()) < 0) {
                log("根据表名"+tableSqlName+"没有找到配置文件");
                return null;
            }
            TableConfig tableConfig = tableConfigSet.getBySqlName(tableSqlName);
            if(tableConfig == null) {
                log("根据表名"+tableSqlName+"没有找到配置文件");
                return null;
            }
            Map map = toMap(tableConfig);
            return Arrays.asList(map);
        }
    }

    private List<Map> toMaps(Collection<TableConfig> tableConfigs) {
        List<Map> result = new ArrayList();
        for(TableConfig c : tableConfigs) {
            result.add(toMap(c));
        }
        return result;
    }
    
    private Map toMap(TableConfig tableConfig) {
        Map map = new HashMap();
        map.putAll(BeanHelper.describe(tableConfig,"sqls"));
        map.put("tableConfig", tableConfig);
        return map;
    }

    private String[] getTableConfigFilesArray() {
        return StringHelper.tokenizeToStringArray(tableConfigFiles, ", \t\n\r\f");
    }

    public void setTableConfigFiles(String tableConfigFiles) {
        this.tableConfigFiles = tableConfigFiles;
    }
    
    public void setTableSqlName(String tableSqlName) {
        this.tableSqlName = tableSqlName;
    }

    private TableConfigSet parseForTableConfigSet() {
        TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(getProject().getBaseDir(), Arrays.asList(getTableConfigFilesArray()));
        return tableConfigSet;
    }
	
}
