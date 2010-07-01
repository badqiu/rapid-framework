package cn.org.rapid_framework.generator;

import java.io.File;

import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorModelUtils;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public class DalgenGeneratorTest extends GeneratorTestCase{

	public void testGenerate() throws Exception{

		Table table = TableFactory.getInstance().getTable("USER_INFO");
		
		g.addTemplateRootDir(new File("template").getAbsoluteFile());
		g.addTemplateRootDir(new File("plugins/dalgen_table//dalgen_table/"));
		g.setExcludes("**/*Manager*.java,**/vo/query/**");
		g.setExcludes("**/*Manager*.java,**/*DaoTest.java,**/*ManagerTest.java");
		generateByTable(table);
		
		GeneratorModel m = GeneratorModelUtils.newFromTable(table);
//		g.deleteBy(m.templateModel, m.filePathModel);
		
//		Runtime.getRuntime().exec("cmd.exe /c start "+new File(GeneratorProperties.getProperty("outRoot")).getAbsolutePath());
	}
	
}
