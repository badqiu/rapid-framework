package cn.org.rapid_framework.generator.ext.ant;

import java.io.File;
import java.util.Arrays;

import cn.org.rapid_framework.generator.ext.tableconfig.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.ext.tableconfig.model.TableConfigSet;
import cn.org.rapid_framework.generator.util.StringHelper;
/**
 * 如果要使用TableConfigSet的GeneratorTask，则可以继承该类
 * 
 * @author badqiu
 *
 */
public abstract class BaseTableConfigSetTask extends BaseGeneratorTask{
	
	private String tableConfigFiles; 
	protected TableConfigSet tableConfigSet;
	

	@Override
	protected void executeBefore() throws Exception {
		super.executeBefore();
		
		if(tableConfigSet == null) {
			tableConfigSet = parseForTableConfigSet(getPackage(), getProject().getBaseDir().getAbsoluteFile(), tableConfigFiles);
		}
		
		if(tableConfigSet == null) {
			throw new Exception("'tableConfigFiles' or 'tableConfigSet' must be not null");
		}
	}

	public void setTableConfigFiles(String tableConfigFiles) {
		this.tableConfigFiles = tableConfigFiles;
	}

	public void setTableConfigSet(TableConfigSet tableConfigSet) {
		this.tableConfigSet = tableConfigSet;
	}
	
    static TableConfigSet parseForTableConfigSet(String _package,File basedir,String tableConfigFiles) {
    	String[] tableConfigFilesArray = StringHelper.tokenizeToStringArray(tableConfigFiles, ", \t\n\r\f");
        TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(basedir, Arrays.asList(tableConfigFilesArray));
        tableConfigSet.setPackage(_package);
        return tableConfigSet;
    }
}
