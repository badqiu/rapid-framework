package cn.org.rapid_framework.web.tags;

/**
 * @author badqiu
 */
class Utils {
	
	public static String BLOCK = "__jsp_override__";
	
	static String getOverrideVariableName(String name) {
		return BLOCK + name;
	}
	
	
}
