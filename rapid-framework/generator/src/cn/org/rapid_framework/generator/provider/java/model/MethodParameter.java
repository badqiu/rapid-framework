package cn.org.rapid_framework.generator.provider.java.model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.StringHelper;


public class MethodParameter {
	int paramIndex = -1;
	String paramName;
	JavaClass paramClass; //parameter的类型
	JavaMethod method; //与parameter相关联的method
	
	public MethodParameter(int paramIndex, JavaMethod method,JavaClass paramClazz) {
		super();
		this.paramIndex = paramIndex;
		this.paramClass = paramClazz;
	}

    public JavaMethod getMethod() {
		return method;
	}
    
	public String getName() {
	    //return "param"+paramIndex;
	    if(StringHelper.isNotBlank(paramName))
	        return paramName;
	    if(paramClass.getClazz().isPrimitive() || paramClass.getClazz().getName().startsWith("java.")) {
	        return "param"+paramIndex;
	    }else {
	        return StringHelper.uncapitalize(paramClass.getClassName());
	    }
	}

	public int getParamIndex() {
		return paramIndex;
	}

	public String getAsType() {
		return paramClass.getAsType();
	}

	public String getClassName() {
		return paramClass.getClassName();
	}

	public String getJavaType() {
		return paramClass.getJavaType();
	}

	public String getPackageName() {
		return paramClass.getPackageName();
	}

	public String getPackagePath() {
		return paramClass.getPackagePath();
	}

	public String getParentPackageName() {
		return paramClass.getParentPackageName();
	}

	public String getParentPackagePath() {
		return paramClass.getParentPackagePath();
	}

    public boolean isArray() {
    	return paramClass.isArray();
    }
    
	public String getCanonicalName() {
		return paramClass.getCanonicalName();
	}

	public List<JavaField> getFields() {
		return paramClass.getFields();
	}

	public JavaMethod[] getMethods() {
		return paramClass.getMethods();
	}

	public boolean isAnnotation() {
		return paramClass.isAnnotation();
	}

	public boolean isAnonymousClass() {
		return paramClass.isAnonymousClass();
	}

	public boolean isEnum() {
		return paramClass.isEnum();
	}

	public boolean isInterface() {
		return paramClass.isInterface();
	}

	public boolean isLocalClass() {
		return paramClass.isLocalClass();
	}

	public boolean isMemberClass() {
		return paramClass.isMemberClass();
	}

	public boolean isPrimitive() {
		return paramClass.isPrimitive();
	}

	public boolean isSynthetic() {
		return paramClass.isSynthetic();
	}

	public JavaProperty[] getProperties() throws Exception {
		return paramClass.getProperties();
	}

	public String getSuperclassName() {
		return paramClass.getSuperclassName();
	}

	public JavaClass getParamClass() {
        return paramClass;
    }
	
	public static String[] parseJavaFileForParamNames(Method method,File javaFile) throws IOException {
	    String content = IOHelper.readFile(javaFile);
	    return new MethodParametersParser().parseJavaFileForParamNames(method, content);
	}
	
	public static class MethodParametersParser {

        public String[] parseJavaFileForParamNames(Method method,String content) {
            Pattern methodPattern = Pattern.compile(method.getName()+"\\s*\\("+getParamsPattern(method)+"\\)\\s*\\{");
    //        System.out.println(methodPattern);
    	    List<String> methodParams = new ArrayList();
    	    Matcher m = methodPattern.matcher(content);
    	    List paramNames = new ArrayList();
    	    while(m.find()) {
    	        for(int i = 1; i <= method.getParameterTypes().length; i++) {
                    paramNames.add(m.group(i));
                }
    	        return (String[])paramNames.toArray(new String[0]);
    	    }
    	    return null;
        }
    
        private String getParamsPattern(Method method) {
            List paramPatterns = new ArrayList();
    	    for(int i = 0; i < method.getParameterTypes().length; i++ ) {
    	        Class type = method.getParameterTypes()[i];
    	        String paramPattern = "\\s*.*"+type.getSimpleName()+".*\\s+(\\w+)\\s*";
    	        paramPatterns.add(paramPattern);
    	    }
    	    return StringHelper.join(paramPatterns, ",");
        }
        
	}
    
	public String toString() {
		return "MethodParameter:"+getName()+"="+getJavaType();
	}
	
}
