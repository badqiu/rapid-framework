package cn.org.rapid_framework.freemarker.sqlbuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerMacroInvoker extends TestCase{

	public void test() throws TemplateException, IOException {
		HashMap map = new HashMap();
		map.put("usename","badqiu");
		map.put("sex","f");
		map.put("age",18);
		String[] list = new String[8];
		Arrays.fill(list,"vvvv");
		map.put("list", list);
		map.put("StringUtils",new StringUtils());
//		new Template("sql.flt",new FileReader())
		String result = invokeMacro("sql.flt","pageQuery",map);
		System.out.println("generate sql:"+result);
	}
	
	public static String invokeMacro(String templateName,String macro,Object model) throws TemplateException, IOException {
		File file = ResourceUtils.getFile("classpath:sql.flt");
		String macros = IOUtils.toString(new FileReader(file));
		macros = macros + "<@"+macro+"/>";
		Template t = new Template(templateName,new StringReader(macros));
		StringWriter out = new StringWriter();
		t.process(model, out);
		return out.toString();
	}
}
