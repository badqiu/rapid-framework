/**
 * Groovy Maven Plugin Default Variables
 * By default a few variables are bound into the scripts environment:
 * 
 * project	 The maven project, with auto-resolving properties
 * pom	     Alias for project
 * session	 The executing MavenSession
 * settings	 The executing Settings
 * log	     A SLF4J Logger instance
 * ant	     An AntBuilder instance for easy access to Ant tasks
 * fail()	 A helper to throw MojoExecutionException
 **/

import cn.org.rapid_framework.generator.*;
import cn.org.rapid_framework.generator.util.*;
import cn.org.rapid_framework.generator.ext.tableconfig.model.*;
import cn.org.rapid_framework.generator.provider.db.model.*;
import cn.org.rapid_framework.generator.ext.tableconfig.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;

main();

def main() {
	freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);
	
	loadDefaultGeneratorProperties()
	
	String executeTarget = System.getProperty("executeTarget"); 
	println "target:" + executeTarget;
	new Targets(this)."${executeTarget}"();
	
	println "---------------------Generator executed SUCCESS---------------------"
}

def loadDefaultGeneratorProperties() {
	GeneratorProperties.properties.put("generator_tools_class","cn.org.rapid_framework.generator.util.StringHelper,org.apache.commons.lang.StringUtils");
	GeneratorProperties.properties.put("gg_isOverride","true");
	
	GeneratorProperties.properties.put("generator_sourceEncoding","UTF-8");
	GeneratorProperties.properties.put("generator_outputEncoding","UTF-8");
	GeneratorProperties.properties.put("gg_isOverride","true");
	//将表名从复数转换为单数 
	GeneratorProperties.properties.put("tableNameSingularize","true");
	if(pom != null) {
		//加载配置文件
		GeneratorProperties.load("${pom.basedir}/"+System.getProperty("generatorConfigFile")); 
		GeneratorProperties.properties.put("basedir",pom.basedir);
		GeneratorProperties.properties.putAll(pom.properties);
	}else {
		GeneratorProperties.load("db.xml","gen_config.xml");
		GeneratorProperties.properties.put("basedir",new File("."));
	}
}

public class Targets extends HashMap{
	public  Object pom;
	public  Object settings;
	public  Object log;
	public  fail
	
	public Targets(Object global) {
		this.pom = global.pom;
		this.settings = global.settings;
		this.log = global.log;
		this.fail = global.fail;

		for(e in System.properties) {
			put(e.key,e.value)
		}
		for(e in GeneratorProperties.properties) {
			put(e.key,e.value)
		}
		// GeneratorProperties.properties.each { k,v -> println "${k}=${v}"}
	}
	
	def dal() {
		println "dal_package:${dal_package} basedir:${basedir} dir_table_configs:${dir_table_configs}";
		TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(new File(basedir,dir_table_configs),dal_package,Helper.getTableConfigFiles(new File(basedir,dir_table_configs)));
		GenUtils.genByTableConfigSet(Helper.createGeneratorFacade(dir_dal_output_root,"${dir_templates_root}/table_config_set/dal","${dir_templates_root}/share/dal"),tableConfigSet); 
		GenUtils.genByTableConfig(Helper.createGeneratorFacade(dir_dal_output_root,"${dir_templates_root}/table_config/dal","${dir_templates_root}/share/dal"),tableConfigSet,genInputCmd); 
		GenUtils.genByOperation(Helper.createGeneratorFacade(dir_dal_output_root,"${dir_templates_root}/operation/dal","${dir_templates_root}/share/dal"),tableConfigSet,genInputCmd); 
	}
	
	def table() {
		GenUtils.genByTable(Helper.createGeneratorFacade(dir_dal_output_root,"${dir_templates_root}/table/dalgen_config","${dir_templates_root}/share/dal"),genInputCmd)
	}
	
	def seq() {
		TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(new File(basedir,dir_table_configs),dal_package,Helper.getTableConfigFiles(new File(basedir,dir_table_configs)));
		GeneratorProperties.properties.put("basepackage",dal_package);
		GenUtils.genByTableConfigSet(Helper.createGeneratorFacade(dir_dal_output_root,"${dir_templates_root}/table_config_set/sequence","${dir_templates_root}/share/dal"),tableConfigSet); 
	}
	
	def crud() {
		GeneratorProperties.setProperty("basepackage",crud_basepackage);
		
		GeneratorFacade gf = new GeneratorFacade();
	    gf.getGenerator().setTemplateRootDir("${dir_crud_template_root}");
	    gf.getGenerator().setOutRootDir(dir_crud_out_root);
	    
	    gf.deleteOutRootDir();
		GenUtils.genByTable(gf,genInputCmd);
		if(SystemHelper.isWindowsOS) {
			Runtime.getRuntime().exec("cmd.exe /c start ${dir_crud_out_root}")
		}
	}
}

public class GenUtils {
	public static void genByTableConfigSet(GeneratorFacade generatorFacade,TableConfigSet tableConfigSet) throws Exception {
		Map map = new HashMap();
		map.putAll(BeanHelper.describe(tableConfigSet));
		map.put("tableConfigSet", tableConfigSet);
		generatorFacade.generateByMap(map);
	}
	
	public static void genByTableConfig(GeneratorFacade generatorFacade,TableConfigSet tableConfigSet,String tableSqlName) throws Exception {
		
		Collection<TableConfig> tableConfigs = Helper.getTableConfigs(tableConfigSet,tableSqlName);
		for(TableConfig tableConfig : tableConfigs) {
			Map map = new HashMap();
			String[] ignoreProperties = {"sqls"};
	        map.putAll(BeanHelper.describe(tableConfig,ignoreProperties));
	        map.put("tableConfig", tableConfig);
			generatorFacade.generateByMap(map);
		}
	}
	
	public static void genByOperation(GeneratorFacade generatorFacade,TableConfigSet tableConfigSet,String tableSqlName) throws Exception {
		Collection<TableConfig> tableConfigs = Helper.getTableConfigs(tableConfigSet,tableSqlName);
		for(TableConfig tableConfig : tableConfigs) {
			for(Sql sql : tableConfig.getSqls()) {
	            Map operationMap = new HashMap();
	            operationMap.putAll(BeanHelper.describe(sql));
	            operationMap.put("sql", sql);
	            operationMap.put("tableConfig", tableConfig);
	            operationMap.put("basepackage", tableConfig.getBasepackage());
				generatorFacade.generateByMap(operationMap);
	        }
        }
	}
	
	public static void genByTable(GeneratorFacade generatorFacade,String tableSqlName) throws Exception {
		generatorFacade.generateByTable(tableSqlName);
	}

}

public class Helper {
	public static List<String> getTableConfigFiles(File basedir) {
		String[] tableConfigFilesArray = basedir.list();
		List<String> result = new ArrayList();
		for(String str : tableConfigFilesArray) {
			if(str.endsWith(".xml")) {
				result.add(str);
			}
		}
		return result;
	}
	public static Collection<TableConfig> getTableConfigs(TableConfigSet tableConfigSet,String tableSqlName) {
		if("*".equals(tableSqlName)) {
			return tableConfigSet.getTableConfigs();
		}else {
			TableConfig tableConfig = tableConfigSet.getBySqlName(tableSqlName);
			if(tableConfig == null) {
				throw new RuntimeException("根据tableName:${tableSqlName}没有找到相应的配置文件");
			}
			return Arrays.asList(tableConfig);
		}
	}
	
	public static GeneratorFacade createGeneratorFacade(String outRootDir,String... templateRootDirs) {
	    if(templateRootDirs == null) throw new IllegalArgumentException("templateRootDirs must be not null");
	    if(outRootDir == null) throw new IllegalArgumentException("outRootDir must be not null");
	    
	    GeneratorFacade gf = new GeneratorFacade();
	    gf.getGenerator().setTemplateRootDirs(templateRootDirs);
	    gf.getGenerator().setOutRootDir(outRootDir);
	    return gf;
	}
		
}

