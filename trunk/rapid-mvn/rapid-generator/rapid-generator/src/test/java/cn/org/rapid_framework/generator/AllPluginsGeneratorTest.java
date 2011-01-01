package cn.org.rapid_framework.generator;

import java.io.File;

import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorContext;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.util.StringHelper;

public class AllPluginsGeneratorTest extends GeneratorTestCase {
	
	//FIXME plugins所有的模板还没有搬迁过来
	public void testGenerate() throws Exception{
		
		GeneratorContext.clear();
		GeneratorContext.put("basepackage","com.company.project");
		GeneratorContext.put("namespace","pages");
		GeneratorContext.put("appModule","shared");
		g.addTemplateRootDir("classpath:generator/template/rapid/table");
		g.addTemplateRootDir("classpath:generator/template/rapid/table/ria_flex_cairngorm");
		g.addTemplateRootDir("classpath:generator/template/rapid/share/basic");
		g.addTemplateRootDir("classpath:generator/template/rapid/share/flex");
		GeneratorProperties.setProperty(GeneratorConstants.GENERATOR_TOOLS_CLASS, StringHelper.class.getName());
		
		generateByTable(TableFactory.getInstance().getTable("USER_INFO"));
		GeneratorContext.clear();
//		Runtime.getRuntime().exec("cmd.exe /c start D:\\webapp-generator-output");
	}
	
//	public void testGenerateForDemo() throws Exception{
//		
//		
//		File[] listFiles = new File("plugins").listFiles();
//		for(int i = 0; i < listFiles.length; i++) {
//			
//			File child = listFiles[i];
//			if(child.isHidden() || !child.isDirectory()) {
//				continue;
//			}
//			File pluginTemplate = new File(child,"template").getAbsoluteFile();
//			if(!pluginTemplate.exists()) {
//				continue;
//			}
//			
//			Generator g = new Generator();
//			
//			boolean includeDaoAndService = true;
//			if(includeDaoAndService) {
//				g.addTemplateRootDir(new File("template"));
//			}else {
//				Collection<File> includeFiles = FileUtils.listFiles(new File("template"), new String[]{"include"}, true);
//				for(File src : includeFiles) {
//					FileUtils.copyFileToDirectory(src, new File("d:/temp/template"));
//				}
//				g.addTemplateRootDir(new File("d:/temp/template"));
//			}
//			
//			String baseTemplateOutput = "d:/generator-demo-output/"+child.getName();
//			g.setOutRootDir(baseTemplateOutput + "/generator-output");
//			
//			FileUtils.copyDirectory(pluginTemplate, new File(baseTemplateOutput+"/template"));
//			System.out.println("pluginTemplate:"+pluginTemplate);
//			
//			g.addTemplateRootDir(pluginTemplate);
//			generateByTable(g,DbTableFactory.getInstance().getTable("USER_INFO"));
//		}
//		
////		Runtime.getRuntime().exec("cmd.exe /c start d:/generator-demo-output");
//	}
//	
}
