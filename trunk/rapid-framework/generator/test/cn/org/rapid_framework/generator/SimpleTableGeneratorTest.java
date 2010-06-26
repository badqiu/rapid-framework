package cn.org.rapid_framework.generator;

import java.io.File;

import cn.org.rapid_framework.generator.provider.db.table.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public class SimpleTableGeneratorTest extends GeneratorTestCase{

	public void testGenerate() throws Exception{

		Table table = DbTableFactory.getInstance().getTable("USER_INFO");
		
		g.addTemplateRootDir(new File("template").getAbsoluteFile());
		g.addTemplateRootDir(new File("plugins/freemarker_html_view/template"));
		
		generateByTable(table);
		
//		Runtime.getRuntime().exec("cmd.exe /c start "+new File(g.outRootDir).getAbsolutePath());
	}
	
}
