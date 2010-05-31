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
import cn.org.rapid_framework.generator.util.ExceptionSummaryUtils;
import cn.org.rapid_framework.generator.util.IOHelper;
/**
 * 
 * @author badqiu
 *
 */
public class GeneratorFacade {
	
	
	public GeneratorFacade() {
	}
	
	public void printAllTableNames() throws Exception {
		List tables = DbTableFactory.getInstance().getAllTables();
		System.out.println("\n----All TableNames BEGIN----");
		for(int i = 0; i < tables.size(); i++ ) {
			String sqlName = ((Table)tables.get(i)).getSqlName();
			System.out.println("g.generateTable(\""+sqlName+"\");");
		}
		System.out.println("----All TableNames END----");
	}
	
	public void generateByAllTable() throws Exception {
		List<Table> tables = DbTableFactory.getInstance().getAllTables();
		List exceptions = new ArrayList();
		for(int i = 0; i < tables.size(); i++ ) {
			exceptions.addAll(generateByTable(createGeneratorForDbTable(),tables.get(i)));
		}
		printExceptionsSumary(exceptions);
	}
	
	public void generateByTable(String tableName) throws Exception {
		Generator g = createGeneratorForDbTable();
		
		Table table = DbTableFactory.getInstance().getTable(tableName);
		printExceptionsSumary(generateByTable(g, table));
	}

	private List<Exception> generateByTable(Generator g, Table table) throws Exception {
		GeneratorModel m = GeneratorModel.newFromTable(table);
		String displayText = table.getSqlName()+" => "+table.getClassName();
		return generateBy(g, m, displayText);
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
		String displayText = "JavaClass:"+clazz.getSimpleName();
		printExceptionsSumary(generateBy(g, m, displayText));
	}

	private List<Exception> generateBy(Generator g, GeneratorModel m, String displayText) throws Exception {
		System.out.println("***************************************************************");
		System.out.println("* BEGIN generate " + displayText);
		System.out.println("***************************************************************");
		List<Exception> exceptions = g.generateBy(m.templateModel,m.filePathModel);
		return exceptions;
	}

	public static void printExceptionsSumary(List<Exception> exceptions) {
		File errorFile = new File(GeneratorProperties.getRequiredProperty("outRoot"),"generator_error.log");
		ExceptionSummaryUtils.printExceptionsSumary(errorFile,"* 输出目录已经生成generator_error.log用于查看错误 ", exceptions);
	}

	public void clean() throws IOException {
		Generator g = createGeneratorForDbTable();
		g.clean();
	}
	
	public Generator createGeneratorForDbTable() {
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
			templateModel.put("env", System.getenv());
			templateModel.put("system", System.getProperties());
			
			Map filePathModel = new HashMap();
			filePathModel.putAll(GeneratorProperties.getProperties());
			filePathModel.putAll(BeanHelper.describe(table));
			return new GeneratorModel(templateModel,filePathModel);
		}
		
		public static GeneratorModel newFromClass(Class clazz) {
			Map templateModel = new HashMap();
			templateModel.putAll(GeneratorProperties.getProperties());
			templateModel.put("clazz", new JavaClass(clazz));
			templateModel.put("env", System.getenv());
			templateModel.put("system", System.getProperties());
			
			Map filePathModel = new HashMap();
			filePathModel.putAll(GeneratorProperties.getProperties());
			filePathModel.putAll(BeanHelper.describe(clazz));
			return new GeneratorModel(templateModel,filePathModel);
		}
	}
}
