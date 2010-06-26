package cn.org.rapid_framework.generator;

import java.io.File;

import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public class Struts2GeneratorTest extends GeneratorTestCase{

	public void testGenerate() throws Exception{
		
		Table table = TableFactory.getInstance().getTable("USER_INFO");
		
		g.addTemplateRootDir(new File("template").getAbsoluteFile());
		g.addTemplateRootDir(new File("plugins/struts2/template"));
		
		generateByTable(table);
		
//		Runtime.getRuntime().exec("cmd.exe /c start "+new File(g.outRootDir).getAbsolutePath());
	}
	
}
