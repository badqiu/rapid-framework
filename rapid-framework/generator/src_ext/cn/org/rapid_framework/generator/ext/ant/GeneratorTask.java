package cn.org.rapid_framework.generator.ext.ant;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.ext.config.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfig;
import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfigSet;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.StringHelper;

public class GeneratorTask extends Task {
	String tables = "";
	String genInputCmd = "";
	
	private String tableInput;
	private String tableOutput;
	private String operationInput;
	private String operationOutput;
	private String sequenceInput;
	private String sequenceOutput;
	
	@Override
	public void execute() throws BuildException {
		try {
			execute0();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	private void execute0() throws Exception {
		if("*".equals(genInputCmd)) {
		    generateByAllTable();
		}else if("seq".equals(genInputCmd)) {
		    generateBySequence();
		}else {
		    generateByTable(genInputCmd);
		}
	}

    private GeneratorFacade createGeneratorFacade(String input,String output) {
        GeneratorFacade gf = new GeneratorFacade();
		GeneratorProperties.setProperties(new Properties());
		Properties properties = toProperties(getProject().getProperties());
		properties.setProperty("basedir", getProject().getBaseDir().getAbsolutePath());
		GeneratorProperties.setProperties(properties);
		gf.g.addTemplateRootDir(new File(input));
        gf.g.setOutRootDir(output);
        return gf;
    }
	
	private void generateByTable(String tableSqlName) throws Exception {
	    TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(getProject().getBaseDir(), Arrays.asList(getTables()));
	    
        //1. 得到table 输入目录
        //2. 得到table 输出目录
        //3. 创建tableConfig并运行	    
	    TableConfig tableConfig = tableConfigSet.getBySqlName(tableSqlName);
        GeneratorFacade tableGenerator = createGeneratorFacade(tableInput,tableOutput);
        
        Map tableMap = new HashMap();
        tableMap.put("tableConfig", tableConfig);
        tableMap.put("basepackage", tableConfig.getBasepackage());
        tableMap.put("basepackage_dir", tableConfig.getBasepackage_dir());
        tableGenerator.generateByMap(tableMap, tableInput);
        

        //1. 得到 operation 输入目录
        //2. 得到 operation 输出目录
        //3. 创建 sql for operation 并运行	    
        GeneratorFacade operationGenerator = createGeneratorFacade(operationInput,operationOutput);
        for(Sql sql : tableConfig.getSqls()) {
            Map operationMap = new HashMap();
            operationMap.put("sql", sql);
            operationMap.put("basepackage", tableConfig.getBasepackage());
            operationMap.put("basepackage_dir", tableConfig.getBasepackage_dir());
            operationGenerator.generateByMap(operationMap, operationInput);
        }

    }

    private void generateBySequence() throws Exception {
        //1. 得到seq 输入目录
        //2. 得到seq 输出目录
        //3. 创建tableConfigSet并运行
        TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(getProject().getBaseDir(), Arrays.asList(getTables()));
        GeneratorFacade generator = createGeneratorFacade(sequenceInput,sequenceOutput);
        Map tableMap = new HashMap();
        tableMap.put("tableConfigSet", tableConfigSet);
        generator.generateByMap(tableMap, sequenceInput);
    }

    private void generateByAllTable() throws Exception {
        TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(getProject().getBaseDir(), Arrays.asList(getTables()));
        for(TableConfig t : tableConfigSet) {
            generateByTable(t.getSqlname());
        }
    }

    public String[] getTables() {
        return StringHelper.tokenizeToStringArray(tables, ", \t\n\r\f");
    }

    public void setTables(String tables) {
        this.tables = tables;
    }

	private static Properties toProperties(Hashtable properties) {
		Properties props = new Properties();
		props.putAll(properties);
		return props;
	}
	
}
