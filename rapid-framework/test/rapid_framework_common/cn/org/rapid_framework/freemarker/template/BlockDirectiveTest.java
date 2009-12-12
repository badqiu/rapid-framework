package cn.org.rapid_framework.freemarker.template;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import cn.org.rapid_framework.freemarker.FreemarkerTemplateException;
import cn.org.rapid_framework.freemarker.FreemarkerTemplateProcessor;
import freemarker.template.Configuration;

public class BlockDirectiveTest {
	Configuration conf = new Configuration();
	FreemarkerTemplateProcessor processor = new FreemarkerTemplateProcessor();
	
	@Before
	public void setUp() throws FileNotFoundException, IOException{
		processor.setConfiguration(conf);
		conf.setSharedVariable("block", new BlockDirective());
		conf.setSharedVariable("override", new OverrideDirective());
		conf.setSharedVariable("extends", new ExtendsDirective());
		File dir = ResourceUtils.getFile("classpath:fortest_freemarker");
		conf.setDirectoryForTemplateLoading(dir);
		System.out.println(dir.getAbsolutePath());
	}
	
	@Test
	public void testOverride() throws FileNotFoundException, IOException {

		
		assertEquals("<html><head>base_head_content</head><body>base_body_content</body></html>",processor.processTemplate("base.flt", new HashMap()));
		assertEquals("<html><head>base_head_content</head><body>child_body_content</body></html>",processor.processTemplate("child.flt", new HashMap()).trim());
		assertEquals("<html><head>grandchild_head_content</head><body>grandchild_body_content</body></html>",processor.processTemplate("grandchild.flt", new HashMap()).trim());
		assertEquals("<html><head>base_head_content</head><body>base_body_content</body></html>",processor.processTemplate("base-ext.flt", new HashMap()));
	}
	
	@Test(expected=FreemarkerTemplateException.class)
	public void testDirective() {
		assertEquals("",processor.processTemplate("all-directive-test.flt", new HashMap()));
	}
}
