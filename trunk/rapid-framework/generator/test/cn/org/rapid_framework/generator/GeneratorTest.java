package cn.org.rapid_framework.generator;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.DbTableGeneratorModelProvider;
import cn.org.rapid_framework.generator.provider.db.model.Table;

public class GeneratorTest extends TestCase {
	
	public void testGenerate() throws Exception{
		Generator g = new Generator();
		g.outRootDir = PropertiesProvider.getProperties().getProperty("outRoot");
		
		g.addTemplateRootDir(new File("template").getAbsoluteFile());
		File f = new File("plugins");
		File[] listFiles = f.listFiles();
		for(int i = 0; i < listFiles.length; i++) {
			File child = listFiles[i];
			if(child.isHidden() || !child.isDirectory()) {
				continue;
			}
			g.addTemplateRootDir(new File(child,"template").getAbsoluteFile());
		}
		
		List<Table> allTables = DbTableFactory.getInstance().getAllTables();
		for(Table t : allTables) {
			g.generateByModelProvider(new DbTableGeneratorModelProvider(t));
		}
		Runtime.getRuntime().exec("cmd.exe /c start D:\\webapp-generator-output");
	}
	
}
