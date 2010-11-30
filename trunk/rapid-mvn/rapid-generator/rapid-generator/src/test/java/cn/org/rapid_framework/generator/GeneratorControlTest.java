package cn.org.rapid_framework.generator;

import java.util.HashMap;

import cn.org.rapid_framework.generator.util.FileHelper;

public class GeneratorControlTest extends GeneratorTestCase {
	public void setUp() throws Exception {
		super.setUp();
		
		g.setOutRootDir("target/temp/generator_control");
		g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_test_gg_loadxml/template"));
		
	}
	//
	public void testLoadXML() throws Exception {
		g.generateBy(new HashMap(), new HashMap());
	}
}
