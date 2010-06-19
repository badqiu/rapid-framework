package cn.org.rapid_framework.generator;


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

import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.provider.java.model.JavaClass;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.GeneratorException;
/**
 * 
 * @author badqiu
 *
 */
public class GeneratorFacade {
	public Generator g = new Generator();
	public static void printAllTableNames() throws Exception {
		PrintUtils.printAllTableNames(DbTableFactory.getInstance().getAllTables());
	}
	
	public void deleteOutRootDir() throws IOException {
		g.setOutRootDir(GeneratorProperties.getRequiredProperty("outRoot"));
		g.deleteOutRootDir();
	}
	
	public void deleteByAllTable(String templateRootDir) throws Exception {
		List<Table> tables = DbTableFactory.getInstance().getAllTables();
		List exceptions = new ArrayList();
		for(int i = 0; i < tables.size(); i++ ) {
			try {
				deleteByTable(tables.get(i).getSqlName(),templateRootDir);
			}catch(GeneratorException ge) {
				exceptions.addAll(ge.getExceptions());
			}catch(Exception e) {
				exceptions.add(e);
			}
		}
		PrintUtils.printExceptionsSumary("",exceptions);		
	}
	
	public void generateByAllTable(String templateRootDir) throws Exception {
		List<Table> tables = DbTableFactory.getInstance().getAllTables();
		List exceptions = new ArrayList();
		for(int i = 0; i < tables.size(); i++ ) {
			try {
				generateByTable(getGenerator(templateRootDir),tables.get(i));
			}catch(GeneratorException ge) {
				exceptions.addAll(ge.getExceptions());
			}
		}
		PrintUtils.printExceptionsSumary("",exceptions);
	}
	
    public void generateByTable(String tableName,String templateRootDir) throws Exception {
    	if("*".equals(tableName)) {
    		generateByAllTable(templateRootDir);
    		return;
    	}
		Generator g = getGenerator(templateRootDir);
		
		Table table = DbTableFactory.getInstance().getTable(tableName);
		try {
			generateByTable(g, table);
		}catch(GeneratorException ge) {
			PrintUtils.printExceptionsSumary(ge.getMessage(),ge.getExceptions());
		}
	}

    private void generateByTable(Generator g, Table table) throws Exception {
        GeneratorModel m = GeneratorModelUtils.newFromTable(table);
        PrintUtils.printBeginGenerate(table.getSqlName()+" => "+table.getClassName());
        g.generateBy(m.templateModel,m.filePathModel);
    }

    public void deleteByTable(String tableName,String templateRootDir) throws Exception {
    	if("*".equals(tableName)) {
    		deleteByAllTable(templateRootDir);
    		return;
    	}
		Generator g = getGenerator(templateRootDir);
		
		Table table = DbTableFactory.getInstance().getTable(tableName);
		GeneratorModel m = GeneratorModelUtils.newFromTable(table);
		g.deleteBy(m.templateModel, m.filePathModel);
	}
    
	public void generateByClass(Class clazz,String templateRootDir) throws Exception {
		Generator g = getGenerator(templateRootDir);
		GeneratorModel m = GeneratorModelUtils.newFromClass(clazz);
		PrintUtils.printBeginGenerate("JavaClass:"+clazz.getSimpleName());
		try {
			g.generateBy(m.templateModel, m.filePathModel);
		}catch(GeneratorException ge) {
			PrintUtils.printExceptionsSumary(ge.getMessage(),ge.getExceptions());
		}
	}
    
    private Generator getGenerator(String templateRootDir) {
        g.setTemplateRootDir(new File(templateRootDir).getAbsoluteFile());
        g.setOutRootDir(GeneratorProperties.getRequiredProperty("outRoot"));
        return g;
    }
	
	public static class GeneratorModelUtils {
		
		public static GeneratorModel newFromTable(Table table) {
			Map templateModel = new HashMap();
			templateModel.putAll(GeneratorProperties.getProperties());
			templateModel.put("table", table);
			setShareVars(templateModel);
			
			Map filePathModel = new HashMap();
			setShareVars(filePathModel);
			filePathModel.putAll(GeneratorProperties.getProperties());
			filePathModel.putAll(BeanHelper.describe(table));
			return new GeneratorModel(templateModel,filePathModel);
		}

		public static GeneratorModel newFromClass(Class clazz) {
			Map templateModel = new HashMap();
			templateModel.putAll(GeneratorProperties.getProperties());
			templateModel.put("clazz", new JavaClass(clazz));
			setShareVars(templateModel);
			
			Map filePathModel = new HashMap();
			setShareVars(filePathModel);
			filePathModel.putAll(GeneratorProperties.getProperties());
			filePathModel.putAll(BeanHelper.describe(new JavaClass(clazz)));
			return new GeneratorModel(templateModel,filePathModel);
		}
		
		private static void setShareVars(Map templateModel) {
			templateModel.putAll(System.getProperties());
			templateModel.put("env", System.getenv());
			templateModel.put("now", new Date());
		}
	}
	
	private static class PrintUtils {
		
		private static void printExceptionsSumary(String msg,List<Exception> exceptions) throws FileNotFoundException {
			File errorFile = new File(GeneratorProperties.getRequiredProperty("outRoot"),"generator_error.log");
			if(exceptions != null && exceptions.size() > 0) {
				System.err.println("[Generate Error Summary] : "+msg);
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
		
		private static void printBeginGenerate(String displayText) {
			GLogger.println("***************************************************************");
			GLogger.println("* BEGIN generate " + displayText);
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
