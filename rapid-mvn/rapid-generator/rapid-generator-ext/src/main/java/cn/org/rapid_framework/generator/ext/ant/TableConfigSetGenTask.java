package cn.org.rapid_framework.generator.ext.ant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.generator.ext.tableconfig.model.TableConfigSet;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.StringHelper;

public class TableConfigSetGenTask extends BaseGeneratorTask {
	private String tableConfigFiles; 
	
	@Override
    protected List<Map> getGeneratorContexts() {
        TableConfigSet tableConfigSet = parseForTableConfigSet(getPackage(),getProject().getBaseDir(),getTableConfigFilesArray());
        Map map = new HashMap();
        map.putAll(BeanHelper.describe(tableConfigSet));
        map.put("tableConfigSet", tableConfigSet);
        return Arrays.asList(map);
    }

    private String[] getTableConfigFilesArray() {
        return StringHelper.tokenizeToStringArray(tableConfigFiles, ", \t\n\r\f");
    }

    public void setTableConfigFiles(String tableConfigFiles) {
        this.tableConfigFiles = tableConfigFiles;
    }
	
}
