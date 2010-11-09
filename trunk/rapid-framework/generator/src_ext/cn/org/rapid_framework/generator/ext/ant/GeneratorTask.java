package cn.org.rapid_framework.generator.ext.ant;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.ext.config.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfig;
import cn.org.rapid_framework.generator.ext.ibatis.model.TableConfigSet;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.StringHelper;

public class GeneratorTask extends Task {
	private String tableConfigFiles; 
	private String genInputCmd;
	
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
		    error(e);
			throw new BuildException(e);
		}
	}

    private void error(Exception e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        log(out.toString(),Project.MSG_ERR);
    }

	private void execute0() throws Exception {
		if("*".equals(genInputCmd)) {
		    generateByAllTable();
		}else if("seq".equals(genInputCmd)) {
		    generateBySequence();
		}else {
		    TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(getProject().getBaseDir(), Arrays.asList(getTableConfigFilesArray()));
            generateByTable(tableConfigSet,genInputCmd);
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
	
	private void generateByTable(TableConfigSet tableConfigSet,String tableSqlName) throws Exception {
	    
        //1. 得到table 输入目录
        //2. 得到table 输出目录
        //3. 创建tableConfig并运行	    
	    TableConfig tableConfig = tableConfigSet.getBySqlName(tableSqlName);
	    if(tableConfig == null) {
	        log("指定的表没有找到,请重新操作.table:"+tableSqlName,Project.MSG_INFO);
	        return;
	    }
	    
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
        log("生成成功.table:"+tableSqlName,Project.MSG_INFO);
    }

    private void generateBySequence() throws Exception {
        //1. 得到seq 输入目录
        //2. 得到seq 输出目录
        //3. 创建tableConfigSet并运行
        TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(getProject().getBaseDir(), Arrays.asList(getTableConfigFilesArray()));
        GeneratorFacade generator = createGeneratorFacade(sequenceInput,sequenceOutput);
        Map tableMap = new HashMap();
        tableMap.put("tableConfigSet", tableConfigSet);
        generator.generateByMap(tableMap, sequenceInput);
        log("根据sequence生成代码成功.",Project.MSG_INFO);
    }

    private void generateByAllTable() throws Exception {
        TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(getProject().getBaseDir(), Arrays.asList(getTableConfigFilesArray()));
        for(TableConfig t : tableConfigSet) {
            generateByTable(tableConfigSet,t.getSqlname());
        }
    }

    public String[] getTableConfigFilesArray() {
        return StringHelper.tokenizeToStringArray(tableConfigFiles, ", \t\n\r\f");
    }

    public void setTableConfigFiles(String tables) {
        this.tableConfigFiles = tables;
    }
    
	public void setGenInputCmd(String genInputCmd) {
        this.genInputCmd = genInputCmd;
    }

    public void setTableInput(String tableInput) {
        this.tableInput = tableInput;
    }

    public void setTableOutput(String tableOutput) {
        this.tableOutput = tableOutput;
    }

    public void setOperationInput(String operationInput) {
        this.operationInput = operationInput;
    }

    public void setOperationOutput(String operationOutput) {
        this.operationOutput = operationOutput;
    }

    public void setSequenceInput(String sequenceInput) {
        this.sequenceInput = sequenceInput;
    }

    public void setSequenceOutput(String sequenceOutput) {
        this.sequenceOutput = sequenceOutput;
    }
    
    

    private static Properties toProperties(Hashtable properties) {
		Properties props = new Properties();
		props.putAll(properties);
		return props;
	}
	
}
