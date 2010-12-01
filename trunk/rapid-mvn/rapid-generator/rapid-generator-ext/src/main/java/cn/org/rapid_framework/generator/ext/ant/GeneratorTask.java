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
import cn.org.rapid_framework.generator.ext.tableconfig.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.ext.tableconfig.model.TableConfig;
import cn.org.rapid_framework.generator.ext.tableconfig.model.TableConfigSet;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.StringHelper;

public class GeneratorTask extends Task {
	private String tableConfigFiles; 
	private String genInputCmd;
	
	private File tableConfigInput;
	private File tableConfigOutput;
	private File operationInput;
	private File operationOutput;
	private File sequenceInput;
	private File sequenceOutput;
	private File tableInput;
	private File tableOutput;
	
	private File shareInput;
	
	@Override
	public void execute() throws BuildException {
	    super.execute();
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
		    generateByAllTableConfig();
		}else if("seq".equals(genInputCmd)) {
		    generateBySequence();
		}else if("tableConfigSet".equals(genInputCmd)) {
		    generateByTableConfigSet();
        }else if(genInputCmd.startsWith("table ")) {
            generateByTable(genInputCmd);		    
		}else {
		    generateByTableConfigs(parseForTableConfigSet(),genInputCmd);
		}
	}

	private void generateByTable(String genInputCmd) throws Exception {
	    if(tableInput != null) {
    	    GeneratorFacade gf = createGeneratorFacade(tableInput, tableOutput);
    	    String[] args = genInputCmd.split("\\s");
    	    if(args.length <= 1) {
    	        log("请输入要生成的表名");
    	        return;
    	    }
            gf.generateByTable(args[1]);
	    }
    }

    private void generateByTableConfigs(TableConfigSet tableConfigSet, String genInputCmd) throws Exception {
	    for(String tableSqlName : genInputCmd.split(",")) {
	        generateByTableConfig(tableConfigSet,tableSqlName);
	    }
    }

    private void generateByTableConfigSet() throws Exception {
        TableConfigSet tableConfigSet = parseForTableConfigSet();
        GeneratorFacade generator = createGeneratorFacade(tableInput,tableOutput);
        Map map = new HashMap();
        map.putAll(BeanHelper.describe(tableConfigSet));
        map.put("tableConfigSet", tableConfigSet);
        generator.generateByMap(map);
    }

    private TableConfigSet parseForTableConfigSet() {
		TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(getProject().getBaseDir(), Arrays.asList(getTableConfigFilesArray()));
		return tableConfigSet;
	}

	private void generateByTableConfig(TableConfigSet tableConfigSet,String tableSqlName) throws Exception {
	    if(tableSqlName == null) throw new IllegalArgumentException("tableSqlName must be not null");
	      
	    TableConfig tableConfig = tableConfigSet.getBySqlName(tableSqlName);
	    if(tableConfig == null) {
	        log("指定的表没有找到,请重新操作.table:"+tableSqlName,Project.MSG_INFO);
	        return;
	    }
	    
	    if(tableConfigInput != null) {
            GeneratorFacade tableGenerator = createGeneratorFacade(tableConfigInput,tableConfigOutput);
            Map tableMap = new HashMap();
            tableMap.putAll(BeanHelper.describe(tableConfig));
            tableMap.put("tableConfig", tableConfig);
            tableMap.put("basepackage", tableConfig.getBasepackage());
            tableGenerator.generateByMap(tableMap);
	    }
	    
        if(operationInput != null) {
            GeneratorFacade operationGenerator = createGeneratorFacade(operationInput,operationOutput);
            for(Sql sql : tableConfig.getSqls()) {
                Map operationMap = new HashMap();
                operationMap.putAll(BeanHelper.describe(sql));
                operationMap.put("sql", sql);
                operationMap.put("basepackage", tableConfig.getBasepackage());
                operationGenerator.generateByMap(operationMap);
            }
        }
        log("生成成功.table:"+tableSqlName,Project.MSG_INFO);
    }

    private void generateBySequence() throws Exception {
        if(sequenceInput != null) {
            TableConfigSet tableConfigSet = parseForTableConfigSet();
            GeneratorFacade generator = createGeneratorFacade(sequenceInput,sequenceOutput);
            Map map = new HashMap();
            map.putAll(BeanHelper.describe(tableConfigSet));
            map.put("tableConfigSet", tableConfigSet);
            generator.generateByMap(map);
            log("根据sequence生成代码成功.",Project.MSG_INFO);
        }
    }

    private void generateByAllTableConfig() throws Exception {
        TableConfigSet tableConfigSet = parseForTableConfigSet();
        for(TableConfig t : tableConfigSet) {
            generateByTableConfig(tableConfigSet,t.getSqlName());
        }
    }

    GeneratorFacade createGeneratorFacade(File input,File output) {
        if(input == null) throw new IllegalArgumentException("input must be not null");
        if(output == null) throw new IllegalArgumentException("output must be not null");
        
        GeneratorFacade gf = new GeneratorFacade();
        GeneratorProperties.setProperties(new Properties());
        Properties properties = toProperties(getProject().getProperties());
        properties.setProperty("basedir", getProject().getBaseDir().getAbsolutePath());
        GeneratorProperties.setProperties(properties);
        gf.getGenerator().addTemplateRootDir(input);
        if(shareInput != null) {
            gf.getGenerator().addTemplateRootDir(shareInput);
        }
        gf.getGenerator().setOutRootDir(output.getAbsolutePath());
        return gf;
    }
    
    private String[] getTableConfigFilesArray() {
        return StringHelper.tokenizeToStringArray(tableConfigFiles, ", \t\n\r\f");
    }

    public void setTableConfigFiles(String tableConfigFiles) {
        this.tableConfigFiles = tableConfigFiles;
    }

    public void setGenInputCmd(String genInputCmd) {
        this.genInputCmd = genInputCmd;
    }

	public void setTableConfigInput(File tableInput) {
		this.tableConfigInput = tableInput;
	}

	public void setTableConfigOutput(File tableOutput) {
		this.tableConfigOutput = tableOutput;
	}

	public void setOperationInput(File operationInput) {
		this.operationInput = operationInput;
	}

	public void setOperationOutput(File operationOutput) {
		this.operationOutput = operationOutput;
	}

	public void setSequenceInput(File sequenceInput) {
		this.sequenceInput = sequenceInput;
	}

	public void setSequenceOutput(File sequenceOutput) {
		this.sequenceOutput = sequenceOutput;
	}

	public void setShareInput(File shareInput) {
		this.shareInput = shareInput;
	}
	
	public void setTableInput(File v) {
	    assertNotNull(v);
        this.tableInput = v;
    }

    public void setTableOutput(File v) {
        this.tableOutput = v;
    }

    private static void assertNotNull(File v) {
        if(v == null) throw new IllegalArgumentException("dir must be not null");
    }
    
    private static Properties toProperties(Hashtable properties) {
		Properties props = new Properties();
		props.putAll(properties);
		return props;
	}
	
}
