package cn.org.rapid_framework.web.tags;

class Utils {
	
	public static String BLOCK = "__jsp_override__";
	
	public static String getOverrideVariableName(String name) {
		return BLOCK + name;
	}
	
	
}
