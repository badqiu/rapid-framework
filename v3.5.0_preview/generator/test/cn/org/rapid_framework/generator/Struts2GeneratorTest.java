package cn.org.rapid_framework.generator;

import java.io.File;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.DbTableGeneratorModelProvider;
import cn.org.rapid_framework.generator.provider.db.model.Table;

public class Struts2GeneratorTest extends GeneratorTestCase{

	public void testGenerate() throws Exception{
		Generator g = new Generator();
		g.setOutRootDir(".");
		
		Table table = DbTableFactory.getInstance().getTable("USER_INFO");
		
		g.addTemplateRootDir(new File("template").getAbsoluteFile());
		g.addTemplateRootDir(new File("plugins/struts2/template"));
		
		g.generateByModelProvider(new DbTableGeneratorModelProvider(table));
		
//		Runtime.getRuntime().exec("cmd.exe /c start "+new File(g.outRootDir).getAbsolutePath());
	}
	
}
