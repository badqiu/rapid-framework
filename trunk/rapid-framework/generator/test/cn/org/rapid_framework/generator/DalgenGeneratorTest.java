package cn.org.rapid_framework.generator;

import java.io.File;

import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorModel;
import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.model.Table;

public class DalgenGeneratorTest extends GeneratorTestCase{

	public void testGenerate() throws Exception{

		Table table = DbTableFactory.getInstance().getTable("USER_INFO");
		
		g.addTemplateRootDir(new File("template").getAbsoluteFile());
		g.addTemplateRootDir(new File("plugins/dalgen_table//dalgen_table/"));
		
		generateByTable(table);
		
		GeneratorModel m = GeneratorModel.newFromTable(table);
//		g.deleteBy(m.templateModel, m.filePathModel);
		
//		Runtime.getRuntime().exec("cmd.exe /c start "+new File(GeneratorProperties.getProperty("outRoot")).getAbsolutePath());
	}
	
}
