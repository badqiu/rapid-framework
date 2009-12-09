package cn.org.rapid_framework.freemarker.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;
import org.springframework.util.ResourceUtils;

import cn.org.rapid_framework.freemarker.FreemarkerTemplateProcessor;
import freemarker.template.Configuration;
import static org.junit.Assert.*;

public class BlockDirectiveTest {
	@Test
	public void testOverride() throws FileNotFoundException, IOException {
		FreemarkerTemplateProcessor processor = new FreemarkerTemplateProcessor();
		Configuration conf = new Configuration();
		processor.setConfiguration(conf);
		conf.setSharedVariable("block", new BlockDirective());
		conf.setSharedVariable("override", new OverrideDirective());
		File dir = ResourceUtils.getFile("classpath:fortest_freemarker");
		conf.setDirectoryForTemplateLoading(dir);
		System.out.println(dir.getAbsolutePath());
		
		assertEquals("<html><head>base_head_content</head><body>base_body_content</body></html>",processor.processTemplate("base.flt", new HashMap()));
		assertEquals("<html><head>base_head_content</head><body>child_body_content</body></html>",processor.processTemplate("child.flt", new HashMap()).trim());
		assertEquals("<html><head>grandchild_head_content</head><body>grandchild_body_content</body></html>",processor.processTemplate("grandchild.flt", new HashMap()).trim());
		assertEquals("<html><head>base_head_content</head><body>base_body_content</body></html>",processor.processTemplate("base-ext.flt", new HashMap()));
	}
}
