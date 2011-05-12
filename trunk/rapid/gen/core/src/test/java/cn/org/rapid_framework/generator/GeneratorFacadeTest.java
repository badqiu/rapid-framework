package cn.org.rapid_framework.generator;


import java.util.Date;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorModelUtils;
import cn.org.rapid_framework.generator.provider.java.model.JavaClass;

public class GeneratorFacadeTest extends TestCase {
	GeneratorFacade gf = new GeneratorFacade();
	JavaClass javaClass = new JavaClass(GeneratorFacadeTest.class);
	public void test() {
//		GeneratorProperties.("badqiu_birth", new Date());
		GeneratorProperties.setProperty("badqiu_name", "com.badqiu");
		GeneratorModel gm = GeneratorModelUtils.newGeneratorModel("clazz", javaClass);
		assertEquals(gm.templateModel.get("badqiu_name_dir"),null);
		assertEquals(gm.templateModel.get("badqiu_name"),"com.badqiu");
		assertEquals(gm.templateModel.get("clazz"),javaClass);
		
		assertEquals(gm.filePathModel.get("badqiu_name_dir"),null);
		assertEquals(gm.filePathModel.get("badqiu_name"),"com.badqiu");
		
		assertEquals(gm.filePathModel.get("className"),javaClass.getClassName());
		assertEquals(GeneratorFacadeTest.class,gm.filePathModel.get("clazz"));
	}
}
