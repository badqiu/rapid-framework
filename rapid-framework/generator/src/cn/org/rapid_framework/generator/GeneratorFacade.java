package cn.org.rapid_framework.generator;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.provider.java.model.JavaClass;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.IOHelper;
/**
 * 
 * @author badqiu
 *
 */
public class GeneratorFacade {
	
	public static void printAllTableNames() throws Exception {
		List tables = DbTableFactory.getInstance().getAllTables();
		PrintUtils.printAllTableNames(tables);
	}
	
	public void generateByAllTable() throws Exception {
		List<Table> tables = DbTableFactory.getInstance().getAllTables();
		List exceptions = new ArrayList();
		for(int i = 0; i < tables.size(); i++ ) {
			exceptions.addAll(generateByTable(createGeneratorForDbTable(),tables.get(i)));
		}
		PrintUtils.printExceptionsSumary(exceptions);
	}
	
	public void generateByTable(String tableName) throws Exception {
		Generator g = createGeneratorForDbTable();
		
		Table table = DbTableFactory.getInstance().getTable(tableName);
		PrintUtils.printExceptionsSumary(generateByTable(g, table));
	}

	private List<Exception> generateByTable(Generator g, Table table) throws Exception {
		GeneratorModel m = GeneratorModel.newFromTable(table);
		PrintUtils.printBeginGenerate(table.getSqlName()+" => "+table.getClassName());
		return g.generateBy(m.templateModel,m.filePathModel);
	}
	
	public void generateByTable(String tableName,String className) throws Exception {
		Generator g = createGeneratorForDbTable();
		Table table = DbTableFactory.getInstance().getTable(tableName);
		table.setClassName(className);
		generateByTable(g,table);
	}
	
	public void generateByClass(Class clazz) throws Exception {
		Generator g = createGeneratorForJavaClass();
		GeneratorModel m = GeneratorModel.newFromClass(clazz);
		PrintUtils.printBeginGenerate("JavaClass:"+clazz.getSimpleName());
		PrintUtils.printExceptionsSumary(g.generateBy(m.templateModel, m.filePathModel));
	}

	public void clean() throws IOException {
		Generator g = createGeneratorForDbTable();
		g.clean();
	}

	private Generator createGeneratorForDbTable() {
		Generator g = new Generator();
		g.setTemplateRootDir(new File("template").getAbsoluteFile());
		g.setOutRootDir(GeneratorProperties.getRequiredProperty("outRoot"));
		return g;
	}
	
	private Generator createGeneratorForJavaClass() {
		Generator g = new Generator();
		g.setTemplateRootDir(new File("template/javaclass").getAbsoluteFile());
		g.setOutRootDir(GeneratorProperties.getRequiredProperty("outRoot"));
		return g;
	}
	
	public static class GeneratorModel {
		public Map filePathModel;
		public Map templateModel;
		public GeneratorModel(Map templateModel, Map filePathModel) {
			super();
			this.templateModel = templateModel;
			this.filePathModel = filePathModel;
		}
		
		public static GeneratorModel newFromTable(Table table) {
			Map templateModel = new HashMap();
			templateModel.putAll(GeneratorProperties.getProperties());
			templateModel.put("table", table);
			setShareVars(templateModel);
			
			Map filePathModel = new HashMap();
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
			filePathModel.putAll(GeneratorProperties.getProperties());
			filePathModel.putAll(BeanHelper.describe(new JavaClass(clazz)));
			return new GeneratorModel(templateModel,filePathModel);
		}
		
		private static void setShareVars(Map templateModel) {
			templateModel.put("env", System.getenv());
			templateModel.put("system", System.getProperties());
		}
	}
	
	private static class PrintUtils {
		
		private static void printExceptionsSumary(List<Exception> exceptions) {
			File errorFile = new File(GeneratorProperties.getRequiredProperty("outRoot"),"generator_error.log");
			if(exceptions != null && exceptions.size() > 0) {
				System.err.println("[Generate Error Summary]");
				ByteArrayOutputStream errorLog = new ByteArrayOutputStream();
				for(Exception e : exceptions) {
					System.err.println("[GENERATE ERROR]:"+e);
					e.printStackTrace(new PrintStream(errorLog));
				}
				IOHelper.saveFile(errorFile, errorLog.toString());
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
