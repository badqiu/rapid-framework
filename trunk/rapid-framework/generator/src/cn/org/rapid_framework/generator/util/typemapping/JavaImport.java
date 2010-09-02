package cn.org.rapid_framework.generator.util.typemapping;

import java.util.TreeSet;

import cn.org.rapid_framework.generator.util.StringHelper;

public class JavaImport {
    TreeSet<String> imports = new TreeSet<String>();

    public void addImport(String javaType) {
        if (isNeedImport(javaType)) {
            imports.add(javaType);
        }
    }
    
    public void addImport(JavaImport javaImport) {
    	if(javaImport != null)
    		imports.addAll(javaImport.getImports());
    }

    public TreeSet<String> getImports() {
        return imports;
    }
    
    public static boolean isNeedImport(String type) {
        if (StringHelper.isBlank(type)) {
            return false;
        }
        if("void".equals(type)) {
        	return false;
        }

        if (type.startsWith("java.lang.")) {
            return false;
        }
        
        if(JavaPrimitiveTypeMapping.getPrimitiveTypeOrNull(type) != null) {
        	return false;
        }
        
        if ((type.indexOf(".") < 0) || Character.isLowerCase(StringHelper.getJavaClassSimpleName(type).charAt(0))) {
            return false;
        }

        return true;
    }
}
