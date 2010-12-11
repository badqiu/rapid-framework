import cn.org.rapid_framework.generator.*;
import cn.org.rapid_framework.generator.util.*;
import cn.org.rapid_framework.generator.ext.tableconfig.model.*;
import cn.org.rapid_framework.generator.provider.db.model.*;
import cn.org.rapid_framework.generator.ext.tableconfig.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;

GeneratorProperties.load("${pom.basedir}/db.xml","${pom.basedir}/gen_config.xml");
main();

def main() {
	freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);
	
	String executeCmd = System.getProperty("executeCmd"); 
	new Cmd()."${executeCmd}"();
	
	println "---------------------Generator executed SUCCESS---------------------"
}

public class Cmd {
	public	String dir_templates_root = GeneratorProperties.getProperty('dir_templates_root');
	public	String dir_dal_output_root = GeneratorProperties.getProperty('dir_dal_output_root');
	public	String basedir = System.getProperty('pom.basedir');
	public	String genInputCmd = System.getProperty('genInputCmd');
	
	def dal() {
		TableConfigSet tableConfigSet = Helper.parseTableConfigSet(basedir);
		GenUtils.genByTableConfigSet(Helper.createGeneratorFacade('/table_config_set/dal',dir_dal_output_root,'/share/dal',basedir),tableConfigSet); 
		GenUtils.genByTableConfig(Helper.createGeneratorFacade('/table_config/dal',dir_dal_output_root,'/share/dal',basedir),tableConfigSet,genInputCmd); 
		GenUtils.genByOperation(Helper.createGeneratorFacade('/operation/dal',dir_dal_output_root,'/share/dal',basedir),tableConfigSet,genInputCmd); 
	}
	
	def table() {
		GenUtils.genByTable(Helper.createGeneratorFacade('/table/dalgen_config',dir_dal_output_root,'/share/dal',basedir),genInputCmd)
	}
	
	def seq() {
		TableConfigSet tableConfigSet = Helper.parseTableConfigSet(basedir);
		GenUtils.genByTableConfigSet(Helper.createGeneratorFacade('/table_config_set/sequence',dir_dal_output_root,'/share/dal',basedir),tableConfigSet); 
	}
}

public class GenUtils {
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

public class Helper {
	public static List<TableConfig> getTableConfigs(TableConfigSet tableConfigSet,String tableSqlName) {
		if("*".equals(tableSqlName)) {
			return tableConfigSet.getTableConfigs();
		}else {
			return tableConfigSet.getBySqlName(tableSqlName);
		}
	}
	
	public static TableConfigSet parseTableConfigSet(String basedir) {
		String tableConfigFiles = GeneratorProperties.getProperty('tableConfigFiles');
		String dal_package = GeneratorProperties.getProperty('dal_package');
		return new TableConfigXmlBuilder().parseFromXML(dal_package,basedir,tableConfigFiles,);
	}	
	
	public static GeneratorFacade createGeneratorFacade(String input,String output,String shareInput,String basedir) {
	    if(input == null) throw new IllegalArgumentException("input must be not null");
	    if(output == null) throw new IllegalArgumentException("output must be not null");
	    
	    GeneratorFacade gf = new GeneratorFacade();
	    gf.getGenerator().addTemplateRootDir(input);
	    if(shareInput != null) {
	        gf.getGenerator().addTemplateRootDir(shareInput);
	    }
	    gf.getGenerator().setOutRootDir(output);
	    return gf;
	}
		
}

