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
	GeneratorProperties.load("${pom.basedir}/db.xml","${pom.basedir}/gen_config.xml");
	freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);
	
	String executeTarget = System.getProperty("executeTarget"); 
	new Targets(this)."${executeTarget}"();
	
	println "---------------------Generator executed SUCCESS---------------------"
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
		for(e in pom.properties) {
			put(e.key,e.value)
		}
	}
	
	def dal() {
		println "dal_package:${dal_package}"
		TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(dal_package,basedir,tableConfigFiles);
		GenUtils.genByTableConfigSet(Helper.createGeneratorFacade(dir_dal_output_root,"${dir_templates_root}/table_config_set/dal","${dir_templates_root}/share/dal"),tableConfigSet); 
		GenUtils.genByTableConfig(Helper.createGeneratorFacade(dir_dal_output_root,"${dir_templates_root}/table_config/dal","${dir_templates_root}/share/dal"),tableConfigSet,genInputCmd); 
		GenUtils.genByOperation(Helper.createGeneratorFacade(dir_dal_output_root,"${dir_templates_root}/operation/dal","${dir_templates_root}/share/dal"),tableConfigSet,genInputCmd); 
	}
	
	def table() {
		GenUtils.genByTable(Helper.createGeneratorFacade(dir_dal_output_root,"${dir_templates_root}/table/dalgen_config","${dir_templates_root}/share/dal"),genInputCmd)
	}
	
	def seq() {
		TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(dal_package,basedir,tableConfigFiles);
		GenUtils.genByTableConfigSet(Helper.createGeneratorFacade(dir_dal_output_root,"${dir_templates_root}/table_config_set/sequence","${dir_templates_root}/share/dal"),tableConfigSet); 
	}

	def crud() {
		GeneratorFacade gf = Helper.createGeneratorFacade(dir_dal_output_root,
			"${dir_templates_root}/share/basic",
			"${dir_templates_root}/table/dao_hibernate",
			"${dir_templates_root}/table/dao_hibernate_annotation",
			"${dir_templates_root}/table/service_no_interface",
			"${dir_templates_root}/table/web_struts2");
		GenUtils.genByTable(gf,genInputCmd)
	}
}

public class GenUtils extends Targets {
	public static genByTableConfigSet(GeneratorFacade generatorFacade,TableConfigSet tableConfigSet) {
		Map map = new HashMap();
		map.putAll(BeanHelper.describe(tableConfigSet));
		map.put("tableConfigSet", tableConfigSet);
		generatorFacade.generateByMap(map);
	}
	
	public static genByTableConfig(GeneratorFacade generatorFacade,TableConfigSet tableConfigSet,String tableSqlName) {
		String[] ignoreProperties = "sqls";
		
		List<TableConfig> tableConfigs = Helper.getTableConfigs(tableConfigSet,tableSqlName);
		for(TableConfig tableConfig : tableConfigs) {
			Map map = new HashMap();
	        map.putAll(BeanHelper.describe(tableConfig,ignoreProperties));
	        map.put("tableConfig", tableConfig);
			generatorFacade.generateByMap(map);
		}
	}
	
	public static genByOperation(GeneratorFacade generatorFacade,TableConfigSet tableConfigSet,String tableSqlName) {
		List<TableConfig> tableConfigs = Helper.getTableConfigs(tableConfigSet,tableSqlName);
		for(TableConfig tableConfig : tableConfigs) {
			for(Sql sql : tableConfig.getSqls()) {
	            Map operationMap = new HashMap();
	            operationMap.putAll(BeanHelper.describe(sql));
	            operationMap.put("sql", sql);
	            operationMap.put("tableConfig", tableConfig);
	            operationMap.put("basepackage", tableConfig.getBasepackage());
				generatorFacade.generateByMap(operationMap.put);
	        }
        }
	}
	
	public static genByTable(GeneratorFacade generatorFacade,String tableSqlName) {
		generatorFacade.generateByTable(tableSqlName);
	}

}

public class Helper extends Targets {
	public static List<TableConfig> getTableConfigs(TableConfigSet tableConfigSet,String tableSqlName) {
		if("*".equals(tableSqlName)) {
			return tableConfigSet.getTableConfigs();
		}else {
			return tableConfigSet.getBySqlName(tableSqlName);
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

