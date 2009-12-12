package cn.org.rapid_framework.freemarker.template;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * @author badqiu
 */
public class DirectiveUtils {
	
	public static String BLOCK = "__flt_override__";
	
	public static String getOverrideVariableName(String name) {
		return BLOCK + name;
	}
	
	public static void exposeRapidMacros(Configuration conf) {
		conf.setSharedVariable(new BlockDirective().getName(), new BlockDirective());
		conf.setSharedVariable(new ExtendsDirective().getName(), new ExtendsDirective());
		conf.setSharedVariable(new OverrideDirective().getName(), new OverrideDirective());
	}
	
	static String getRequiredParam(Map params,String key,Environment env) throws TemplateException {
		Object value = params.get(key);
		if(value == null || StringUtils.isEmpty(value.toString())) {
			throw new TemplateException("not found required argument:"+key,env);
		}
		return value.toString();
	}
	
	static String getParam(Map params,String key,Environment env) throws TemplateException {
		Object value = params.get(key);
		if(value == null || StringUtils.isEmpty(value.toString())) {
			return null;
		}
		return value.toString();
	}
}
