package cn.org.rapid_framework.generator;


import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.DbTableGeneratorModelProvider;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.provider.java.JavaClassGeneratorModelProvider;
import cn.org.rapid_framework.generator.provider.java.model.JavaClass;
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
		for(int i = 0; i < tables.size(); i++ ) {
			generateByTable(tables.get(i).getSqlName());
		}
	}
	
	public void generateByTable(String tableName) throws Exception {
		Generator g = createGeneratorForDbTable();
		Table table = DbTableFactory.getInstance().getTable(tableName);
		g.generateByModelProvider(new DbTableGeneratorModelProvider(table));
	}
	
	public void generateByClass(Class clazz) throws Exception {
		Generator g = createGeneratorForJavaClass();
		g.generateByModelProvider(new JavaClassGeneratorModelProvider(new JavaClass(clazz)));
	}
	
	public void clean() throws IOException {
		Generator g = createGeneratorForDbTable();
		g.clean();
	}
	
	public Generator createGeneratorForDbTable() {
		Generator g = new Generator();
		g.setTemplateRootDir(new File("template").getAbsoluteFile());
		g.outRootDir = PropertiesProvider.getProperties().getProperty("outRoot");
		return g;
	}
	
	private Generator createGeneratorForJavaClass() {
		Generator g = new Generator();
		g.setTemplateRootDir(new File("template/javaclass").getAbsoluteFile());
		g.outRootDir = PropertiesProvider.getProperties().getProperty("outRoot");
		return g;
	}
}
