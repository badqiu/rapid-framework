package cn.org.rapid_framework.web.tags;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateException;

class Utils {
	
	public static String BLOCK = "__override__";
	
	public static String getOverrideVariableName(String name) {
		return BLOCK + name;
	}
	
	
}
