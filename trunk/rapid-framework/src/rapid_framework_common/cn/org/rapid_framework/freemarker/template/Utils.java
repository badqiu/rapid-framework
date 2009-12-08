package cn.org.rapid_framework.freemarker.template;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateException;

class Utils {
	
	public static String BLOCK = "___block___@";
	
	public static String getBlockName(String name) {
		String realName = BLOCK + name;
		return realName;
	}
	
	public static String getRequiredParam(Map params,String key,Environment env) throws TemplateException {
		Object value = params.get(key);
		if(value == null || StringUtils.isEmpty(value.toString())) {
			throw new TemplateException("not found required param:"+key,env);
		}
		return value.toString();
	}
}
