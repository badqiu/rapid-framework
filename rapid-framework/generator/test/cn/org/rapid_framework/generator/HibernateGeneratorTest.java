package cn.org.rapid_framework.generator;

import java.io.File;

import junit.framework.TestCase;

import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.DbTableGeneratorModelProvider;
import cn.org.rapid_framework.generator.provider.db.model.Table;

public class HibernateGeneratorTest extends GeneratorTestCase{

	public void testGenerate() throws Exception{
		Generator g = new Generator();
		g.setOutRootDir(".");
		
		Table table = DbTableFactory.getInstance().getTable("USER_INFO");
		
		g.addTemplateRootDir(new File("template").getAbsoluteFile());
		g.addTemplateRootDir(new File("plugins/hibernate/template"));
		
		g.generateByModelProvider(new DbTableGeneratorModelProvider(table));
		
		//Runtime.getRuntime().exec("cmd.exe /c start "+new File(g.outRootDir).getAbsolutePath());
	}
	
	
}
