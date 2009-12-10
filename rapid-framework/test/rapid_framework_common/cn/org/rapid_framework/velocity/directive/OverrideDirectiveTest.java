package cn.org.rapid_framework.velocity.directive;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;


public class OverrideDirectiveTest {
	
	VelocityEngine engine;
	
	@Before
	public void setUp() throws Exception{
		String path = ResourceUtils.getFile("classpath:fortest_velocity").getCanonicalPath().replace('\\', '/')+"/";
		engine = new VelocityEngine();
		Properties p = new Properties();   
		p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
		p.setProperty("userdirective","cn.org.rapid_framework.velocity.directive.BlockDirective,cn.org.rapid_framework.velocity.directive.OverrideDirective,cn.org.rapid_framework.velocity.directive.ExtendsDirective");
		engine.init(p);
	}

	@Test
	public void test() throws Exception {
		
		System.out.println(processTemplate("base.vm"));
		System.out.println(processTemplate("child.vm"));
		System.out.println(processTemplate("grandchild.vm"));
		 
		assertEquals("<html><head>base_head_content</head><body>base_body_content</body></html>",processTemplate("base.vm"));
		assertEquals("<html><head>base_head_content</head><body>child_body_content</body></html>",processTemplate("child.vm"));
		assertEquals("<html><head>grandchild_head_content</head><body>grandchild_body_content</body></html>",processTemplate("grandchild.vm"));
		
//		assertEquals("<html><head>base_head_content</head><body>base_body_content</body></html>",processTemplate("base-ext.vm"));

	}
	
	@Test(expected=ParseErrorException.class)
	public void testArgumentWithOverride() throws ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {
		VelocityContext context = new VelocityContext();
		StringWriter out = new StringWriter();
		engine.evaluate(context,out , "test.vm", "#override() diy \n #end");
		System.out.println(out.toString());
	}
	
	@Test(expected=ParseErrorException.class)
	public void testArgumentWithBlock() throws ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {
		VelocityContext context = new VelocityContext();
		StringWriter out = new StringWriter();
		engine.evaluate(context,out , "test.vm", "#block() diy \n #end");
		System.out.println(out.toString());
	}

	private String processTemplate(String name) throws ResourceNotFoundException, ParseErrorException, Exception {
		String str =  VelocityEngineUtils.mergeTemplateIntoString(engine, name, new HashMap());
		str = StringUtils.replace(str, "\r", "");
		str = StringUtils.replace(str, "\n", "");
		str = StringUtils.replace(str, "\r\n", "");
		str = StringUtils.replace(str, " ", "");
		return str.replaceAll("\\s+", "");
	}
	
}
