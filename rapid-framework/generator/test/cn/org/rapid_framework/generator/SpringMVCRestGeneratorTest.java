package cn.org.rapid_framework.generator;

import java.io.File;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.DbTableGeneratorModelProvider;
import cn.org.rapid_framework.generator.provider.db.model.Table;

public class SpringMVCRestGeneratorTest extends TestCase{

	public void testGenerate() throws Exception{
		Generator g = new Generator();
		g.setOutRootDir(".");
		
		Table table = DbTableFactory.getInstance().getTable("user_info");
		
		g.addTemplateRootDir(new File("template").getAbsoluteFile());
		g.addTemplateRootDir(new File("plugins/springmvc_rest/template"));
		
		g.generateByModelProvider(new DbTableGeneratorModelProvider(table));
		
//		Runtime.getRuntime().exec("cmd.exe /c start "+new File(g.outRootDir).getAbsolutePath());
	}
	
}
