package cn.org.rapid_framework.generator;


import static cn.org.rapid_framework.generator.GeneratorConstants.GENERATOR_TOOLS_CLASS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.provider.java.model.JavaClass;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.ClassHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.GeneratorException;
import cn.org.rapid_framework.generator.util.StringHelper;
/**
 * 生成器的主要入口类,包装相关方法供外部生成代码使用
 * 
 * 使用GeneratorFacade之前，需要设置Generator的相关属性
 * @author badqiu
 *
 */
public class GeneratorFacade  {
	private Generator generator = new Generator();
	
	public GeneratorFacade(){
	    if(StringHelper.isNotBlank(GeneratorProperties.getProperty("outRoot"))) {
	        generator.setOutRootDir(GeneratorProperties.getProperty("outRoot"));
	    }
	}
	
	public static void printAllTableNames() throws Exception {
		PrintUtils.printAllTableNames(TableFactory.getInstance().getAllTables());
	}
	
	public void deleteOutRootDir() throws IOException {
		generator.deleteOutRootDir();
	}
	/**
	 * 自定义变量，生成文件,文件路径与模板引用的变量相同
	 * @throws Exception
	 */		
	public void generateByMap(Map... maps) throws Exception {
		for(Map map : maps) {
			new ProcessUtils().processByMap(map,false);
		}
	}
	/**
	 * 自定义变量，删除生成的文件,文件路径与模板引用的变量相同
	 * @throws Exception
	 */	
	public void deleteByMap(Map... maps) throws Exception {
		for(Map map : maps) {
			new ProcessUtils().processByMap(map, true);
		}
	}
	/**
	 * 自定义变量，生成文件,可以自定义文件路径与模板引用的变量
	 * @throws Exception
	 */	 
    public void generateBy(GeneratorModel... models) throws Exception {
    	for(GeneratorModel model : models) {
    		new ProcessUtils().processByGeneratorModel(model,false);
    	}
    }
	/**
	 * 自定义变量，删除生成的文件,可以自定义文件路径与模板引用的变量
	 * @throws Exception
	 */	    
    public void deleteBy(GeneratorModel... models) throws Exception {
    	for(GeneratorModel model : models) {
    		new ProcessUtils().processByGeneratorModel(model,true);
    	}
    }
	/**
	 * 扫描数据库中所有表对象，然后生成文件,模板引用的变量名称为: table, 实体类为: @see cn.org.rapid_framework.generator.provider.db.table.model.Table
	 * @throws Exception
	 */			   
	public void generateByAllTable() throws Exception {
		new ProcessUtils().processByAllTable(false);
	}
	/**
	 * 扫描数据库中所有表对象，然后删除生成的文件,模板引用的变量名称为: table, 实体类为: @see cn.org.rapid_framework.generator.provider.db.table.model.Table
	 * @throws Exception
	 */		
	public void deleteByAllTable() throws Exception {
		new ProcessUtils().processByAllTable(true);		
	}
	/**
	 * 根据Table生成文件,模板引用的变量名称为: table, 实体类为: @see cn.org.rapid_framework.generator.provider.db.table.model.Table
	 * @throws Exception
	 */	
    public void generateByTable(String... tableNames) throws Exception {
    	for(String tableName : tableNames) {
    		new ProcessUtils().processByTable(tableName,false);
    	}
	}
	/**
	 * 根据Table删除生成的文件,模板引用的变量名称为: table 实体类为: cn.org.rapid_framework.generator.provider.db.table.model.Table
	 * @throws Exception
	 */
    public void deleteByTable(String... tableNames) throws Exception {
    	for(String tableName : tableNames) {
    		new ProcessUtils().processByTable(tableName,true);
    	}
	}
	/**
	 * 根据Class生成文件,模板引用的变量名称为: clazz 实体类为: cn.org.rapid_framework.generator.provider.java.model.JavaClass
	 */    
	public void generateByClass(Class... clazzes) throws Exception {
		for(Class clazz : clazzes) {
			new ProcessUtils().processByClass(clazz,false);
		}
	}
	/**
	 * 根据Class删除生成的文件,模板引用的变量名称为: clazz 实体类为: cn.org.rapid_framework.generator.provider.java.model.JavaClass
	 */
	public void deleteByClass(Class... clazzes) throws Exception {
		for(Class clazz : clazzes) {
			new ProcessUtils().processByClass(clazz,true);
		}
	}
	
	/**
	 * 根据Sql生成文件,模板引用的变量名称为: sql
	 */
	public void generateBySql(Sql... sqls) throws Exception {
		for(Sql sql : sqls) {
			new ProcessUtils().processBySql(sql,false);
		}
	}
	/**
	 * 根据Sql删除生成的文件,模板引用的变量名称为: sql
	 */
	public void deleteBySql(Sql... sqls) throws Exception {
		for(Sql sql : sqls) {
			new ProcessUtils().processBySql(sql,true);
		}
	}
	
    public Generator getGenerator() {
    	return generator;
    }
    
    public void setGenerator(Generator generator) {
    	this.generator = generator;
    }
    
    public class ProcessUtils {
        
        public void processByGeneratorModel(GeneratorModel model,boolean isDelete) throws Exception, FileNotFoundException {
            Generator g = getGenerator();
            
            GeneratorModel targetModel = GeneratorModelUtils.newDefaultGeneratorModel();
            targetModel.filePathModel.putAll(model.filePathModel);
            targetModel.templateModel.putAll(model.templateModel);
            processByGeneratorModel(isDelete, g, targetModel);
        }
        
    	public void processByMap(Map params,boolean isDelete) throws Exception, FileNotFoundException {
			Generator g = getGenerator();
			GeneratorModel m = GeneratorModelUtils.newFromMap(params);
			processByGeneratorModel(isDelete, g, m);
    	}
    	
    	public void processBySql(Sql sql,boolean isDelete) throws Exception {
    		Generator g = getGenerator();
    		GeneratorModel m = GeneratorModelUtils.newGeneratorModel("sql",sql);
    		PrintUtils.printBeginProcess("sql:"+sql.getSourceSql(),isDelete);
    		processByGeneratorModel(isDelete,g,m);
    	}   
    	
    	public void processByClass(Class clazz,boolean isDelete) throws Exception, FileNotFoundException {
			Generator g = getGenerator();
			GeneratorModel m = GeneratorModelUtils.newGeneratorModel("clazz",new JavaClass(clazz));
			PrintUtils.printBeginProcess("JavaClass:"+clazz.getSimpleName(),isDelete);
			processByGeneratorModel(isDelete, g, m);
    	}

        private void processByGeneratorModel(boolean isDelete, Generator g,
                                             GeneratorModel m)
                                                              throws Exception,
                                                              FileNotFoundException {
            try {
				if(isDelete)
					g.deleteBy(m.templateModel, m.filePathModel);
				else
					g.generateBy(m.templateModel, m.filePathModel);
			}catch(GeneratorException ge) {
				PrintUtils.printExceptionsSumary(ge.getMessage(),getGenerator().getOutRootDir(),ge.getExceptions());
				throw ge;
			}
        }
    	
        public void processByTable(String tableName,boolean isDelete) throws Exception {
        	if("*".equals(tableName)) {
        	    if(isDelete)
        	        deleteByAllTable();
        	    else
        	        generateByAllTable();
        		return;
        	}
    		Generator g = getGenerator();
    		Table table = TableFactory.getInstance().getTable(tableName);
    		try {
    			processByTable(g,table,isDelete);
    		}catch(GeneratorException ge) {
    			PrintUtils.printExceptionsSumary(ge.getMessage(),getGenerator().getOutRootDir(),ge.getExceptions());
    			throw ge;
    		}
    	}    
        
		public void processByAllTable(boolean isDelete) throws Exception {
			List<Table> tables = TableFactory.getInstance().getAllTables();
			List exceptions = new ArrayList();
			for(int i = 0; i < tables.size(); i++ ) {
				try {
					processByTable(getGenerator(),tables.get(i),isDelete);
				}catch(GeneratorException ge) {
					exceptions.addAll(ge.getExceptions());
				}
			}
			PrintUtils.printExceptionsSumary("",getGenerator().getOutRootDir(),exceptions);
			if(!exceptions.isEmpty()) {
				throw new GeneratorException("batch generate by all table occer error",exceptions);
			}
			
		}
		
		public void processByTable(Generator g, Table table,boolean isDelete) throws Exception {
	        GeneratorModel m = GeneratorModelUtils.newGeneratorModel("table",table);
	        PrintUtils.printBeginProcess(table.getSqlName()+" => "+table.getClassName(),isDelete);
	        if(isDelete)
	        	g.deleteBy(m.templateModel,m.filePathModel);
	        else 
	        	g.generateBy(m.templateModel,m.filePathModel);
	    }        
    }
	
    @SuppressWarnings("all")
	public static class GeneratorModelUtils {
		
		public static GeneratorModel newGeneratorModel(String key,Object valueObject) {
			GeneratorModel gm = newDefaultGeneratorModel();
			gm.templateModel.put(key, valueObject);
			gm.filePathModel.putAll(BeanHelper.describe(valueObject));
			return gm;
		}
		
		public static GeneratorModel newFromMap(Map params) {
			GeneratorModel gm = newDefaultGeneratorModel();
			gm.templateModel.putAll(params);
			gm.filePathModel.putAll(params);
			return gm;
		}
		
		public static GeneratorModel newDefaultGeneratorModel() {
			Map templateModel = new HashMap();
			templateModel.putAll(getShareVars());
			
			Map filePathModel = new HashMap();
			filePathModel.putAll(getShareVars());
			return new GeneratorModel(templateModel,filePathModel);
		}
		
		public static Map getShareVars() {
			Map templateModel = new HashMap();
			templateModel.putAll(System.getProperties());
			templateModel.putAll(GeneratorProperties.getProperties());
			templateModel.put("env", System.getenv());
			templateModel.put("now", new Date());
			templateModel.put(GeneratorConstants.DATABASE_TYPE.code, GeneratorProperties.getDatabaseType(GeneratorConstants.DATABASE_TYPE.code));
			templateModel.putAll(GeneratorContext.getContext());
			templateModel.putAll(getToolsMap());
			return templateModel;
		}
		
		/** 得到模板可以引用的工具类  */
		private static Map getToolsMap() {
			Map toolsMap = new HashMap();
			String[] tools = GeneratorProperties.getStringArray(GENERATOR_TOOLS_CLASS);
			for(String className : tools) {
				try {
					Object instance = ClassHelper.newInstance(className);
					toolsMap.put(Class.forName(className).getSimpleName(), instance);
					GLogger.debug("put tools class:"+className+" with key:"+Class.forName(className).getSimpleName());
				}catch(Exception e) {
					GLogger.error("cannot load tools by className:"+className+" cause:"+e);
				}
			}
			return toolsMap;
		}

	}
	
	private static class PrintUtils {
		
		private static void printExceptionsSumary(String msg,String outRoot,List<Exception> exceptions) throws FileNotFoundException {
			File errorFile = new File(outRoot,"generator_error.log");
			if(exceptions != null && exceptions.size() > 0) {
				System.err.println("[Generate Error Summary] : "+msg);
				errorFile.getParentFile().mkdirs();
				PrintStream output = new PrintStream(new FileOutputStream(errorFile));
				for(int i = 0; i < exceptions.size(); i++) {
					Exception e = exceptions.get(i);
                    System.err.println("[GENERATE ERROR]:"+e);
					if(i == 0) e.printStackTrace();
					e.printStackTrace(output);
				}
				output.close();
				System.err.println("***************************************************************");
				System.err.println("* "+"* 输出目录已经生成generator_error.log用于查看错误 ");
				System.err.println("***************************************************************");
			}
		}
		
		private static void printBeginProcess(String displayText,boolean isDatele) {
			GLogger.println("***************************************************************");
			GLogger.println("* BEGIN " + (isDatele ? " delete by " : " generate by ")+ displayText);
			GLogger.println("***************************************************************");
		}
		
		public static void printAllTableNames(List<Table> tables) throws Exception {
			GLogger.println("\n----All TableNames BEGIN----");
			for(int i = 0; i < tables.size(); i++ ) {
				String sqlName = ((Table)tables.get(i)).getSqlName();
				GLogger.println("g.generateTable(\""+sqlName+"\");");
			}
			GLogger.println("----All TableNames END----");
		}
	}

}
