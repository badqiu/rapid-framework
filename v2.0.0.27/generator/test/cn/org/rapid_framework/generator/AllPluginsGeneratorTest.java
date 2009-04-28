package cn.org.rapid_framework.generator;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.DbTableGeneratorModelProvider;
import cn.org.rapid_framework.generator.provider.db.model.Table;

public class AllPluginsGeneratorTest extends TestCase {
	
	public void testGenerate() throws Exception{
		Generator g = new Generator();
		g.outRootDir = PropertiesProvider.getProperties().getProperty("outRoot");
		
		g.addTemplateRootDir(new File("template").getAbsoluteFile());
		File[] listFiles = new File("plugins").listFiles();
		for(int i = 0; i < listFiles.length; i++) {
			File child = listFiles[i];
			if(child.isHidden() || !child.isDirectory()) {
				continue;
			}
			File pluginTemplate = new File(child,"template").getAbsoluteFile();
			System.out.println("pluginTemplate:"+pluginTemplate);
			g.addTemplateRootDir(pluginTemplate);
			
		}
		
		List<Table> allTables = DbTableFactory.getInstance().getAllTables();
		for(Table t : allTables) {
			g.generateByModelProvider(new DbTableGeneratorModelProvider(t));
		}
		Runtime.getRuntime().exec("cmd.exe /c start D:\\webapp-generator-output");
	}
	
}
