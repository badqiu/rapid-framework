package cn.org.rapid_framework.generator;

import java.io.File;

import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public class HibernateGeneratorTest extends GeneratorTestCase{

	public void testGenerate() throws Exception{

		Table table = TableFactory.getInstance().getTable("USER_INFO");
		
		g.addTemplateRootDir(new File("template").getAbsoluteFile());
		g.addTemplateRootDir(new File("plugins/hibernate/template"));
		
		generateByTable(table);
		generateByTable(TableFactory.getInstance().getTable("role"));
		generateByTable(TableFactory.getInstance().getTable("blog"));
		
		//Runtime.getRuntime().exec("cmd.exe /c start "+new File(g.outRootDir).getAbsolutePath());
	}
	
	
}
