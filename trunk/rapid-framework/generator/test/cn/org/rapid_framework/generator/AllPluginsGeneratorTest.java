package cn.org.rapid_framework.generator;

import java.io.File;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.DbTableGeneratorModelProvider;
import cn.org.rapid_framework.generator.provider.db.model.Table;

public class AllPluginsGeneratorTest extends TestCase {
	
	public void testGenerate() throws Exception{
		Generator g = new Generator();
		g.outRootDir = GeneratorProperties.getProperties().getProperty("outRoot");
		
		g.addTemplateRootDir(new File("template").getAbsoluteFile());
		File[] listFiles = new File("plugins").listFiles();
		for(int i = 0; i < listFiles.length; i++) {
			File child = listFiles[i];
			if(child.isHidden() || !child.isDirectory()) {
				continue;
			}
			File pluginTemplate = new File(child,"template").getAbsoluteFile();
			System.out.println("pluginTemplate:"+pluginTemplate);
			if(pluginTemplate.exists()) {
				g.addTemplateRootDir(pluginTemplate);
			}
		}
		
		List<Table> allTables = DbTableFactory.getInstance().getAllTables();
		for(Table t : allTables) {
			g.generateByModelProvider(new DbTableGeneratorModelProvider(t));
		}
		Runtime.getRuntime().exec("cmd.exe /c start D:\\webapp-generator-output");
	}
	
	public void testGenerateForDemo() throws Exception{
		
		
		File[] listFiles = new File("plugins").listFiles();
		for(int i = 0; i < listFiles.length; i++) {
			
			File child = listFiles[i];
			if(child.isHidden() || !child.isDirectory()) {
				continue;
			}
			File pluginTemplate = new File(child,"template").getAbsoluteFile();
			
			Generator g = new Generator();
			
			boolean includeDaoAndService = true;
			if(includeDaoAndService) {
				g.addTemplateRootDir(new File("template"));
			}else {
				Collection<File> includeFiles = FileUtils.listFiles(new File("template"), new String[]{"include"}, true);
				for(File src : includeFiles) {
					FileUtils.copyFileToDirectory(src, new File("d:/temp/template"));
				}
				g.addTemplateRootDir(new File("d:/temp/template"));
			}
			
			String baseTemplateOutput = "d:/generator-demo-output/"+child.getName();
			g.outRootDir = baseTemplateOutput + "/generator-output";
			
			FileUtils.copyDirectory(pluginTemplate, new File(baseTemplateOutput+"/template"));
			System.out.println("pluginTemplate:"+pluginTemplate);
			
			g.addTemplateRootDir(pluginTemplate);
			g.generateByModelProvider(new DbTableGeneratorModelProvider(DbTableFactory.getInstance().getTable("blog")));
		}
		
		Runtime.getRuntime().exec("cmd.exe /c start d:/generator-demo-output");
	}
	
}
