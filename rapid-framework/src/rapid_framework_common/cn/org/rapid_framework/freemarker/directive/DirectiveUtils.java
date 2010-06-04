package cn.org.rapid_framework.freemarker.directive; 

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

/**
 * @author badqiu
 */
public class DirectiveUtils {
	
	public static String BLOCK = "__ftl_override__";
	
	public static String getOverrideVariableName(String name) {
		return BLOCK + name;
	}
	
	public static void exposeRapidMacros(Configuration conf) {
		conf.setSharedVariable(BlockDirective.DIRECTIVE_NAME, new BlockDirective());
		conf.setSharedVariable(ExtendsDirective.DIRECTIVE_NAME, new ExtendsDirective());
		conf.setSharedVariable(OverrideDirective.DIRECTIVE_NAME, new OverrideDirective());
	}
	
	static String getRequiredParam(Map params,String key) throws TemplateException {
		Object value = params.get(key);
		if(value == null || StringUtils.isEmpty(value.toString())) {
			throw new TemplateModelException("not found required parameter:"+key+" for directive");
		}
		return value.toString();
	}
	
	static String getParam(Map params,String key,String defaultValue) throws TemplateException {
		Object value = params.get(key);
		return value == null ? defaultValue : value.toString();
	}
}
