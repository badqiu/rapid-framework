package cn.org.rapid_framework.velocity.directive;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.velocity.Template;
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


public class OverrideDirectiveTest {
	
	VelocityEngine engine;
	
	@Before
	public void setUp() throws Exception{
		String path = ResourceUtils.getFile("classpath:fortest_velocity").getCanonicalPath().replace('\\', '/')+"/";
		engine = new VelocityEngine();
		Properties p = new Properties();   
		p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
		p.setProperty("userdirective","cn.org.rapid_framework.velocity.directive.BlockDirective,cn.org.rapid_framework.velocity.directive.OverrideDirective,cn.org.rapid_framework.velocity.directive.ExtendsDirective");
		p.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, "true");
		engine.init(p);
	}

	@Test
	public void test() throws Exception {
		
		System.out.println(VelocityEngineUtils.mergeTemplateIntoString(engine,"base.vm",new HashMap()));
		System.out.println(VelocityEngineUtils.mergeTemplateIntoString(engine,"child.vm",new HashMap()));
		System.out.println(VelocityEngineUtils.mergeTemplateIntoString(engine,"grandchild.vm",new HashMap()));
		 
		assertEquals("<html><head>base_head_content</head><body>base_body_content</body></html>",processTemplate("base.vm"));
		assertEquals("<html><head>base_head_content</head><body><divclass='content'>PoweredByrapid-framework</div></body></html>",processTemplate("child.vm"));
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
	
	@Test(timeout=7000)
	public void testPerformance() throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, Exception {
		
		long start = System.currentTimeMillis();
		int count = 100000;
		String content = RandomStringUtils.random(8192);
		for(int i = 0; i < count; i++) {
			HashMap hashMap = new HashMap();
			hashMap.put("content", content);
			if(i % 100 == 0) {
				System.out.println("current:"+i);
			}
			Template t = engine.getTemplate("performance.vm");
			t.merge(new VelocityContext(hashMap), NULL_WRITER);
//			engine.mergeTemplate("base.vm","UTF-8",new VelocityContext(hashMap),NULL_WRITER);
		}
		float cost = System.currentTimeMillis() - start;
		System.out.println(cost+" "+ (count/(cost/1000))+" ");
	}

	private String processTemplate(String name) throws ResourceNotFoundException, ParseErrorException, Exception {
		String str =  VelocityEngineUtils.mergeTemplateIntoString(engine, name, new HashMap());
		return str.replaceAll("\\s+", "");
	}
	
	Writer NULL_WRITER = new Writer() {
		@Override
		public void close()   {
		}
		@Override
		public void flush()   {
		}
		@Override
		public void write(char[] cbuf, int off, int len){
		}
	};
}
