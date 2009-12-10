package cn.org.rapid_framework.velocity.directive;

class Utils {
	
	static String BLOCK = "__vm_override__";
	
	static String getOverrideVariableName(String name) {
		return BLOCK + name;
	}
	
	
}
