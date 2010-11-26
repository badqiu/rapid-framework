/**
 * project:pomer
 * 
 * Copyright 2008 [pomer], Inc. All rights reserved.
 * Website: http://www.pomer.org.cn/
 * 
 */
package cn.org.rapid_framework.generator.provider.java.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.rapid_framework.generator.provider.java.model.MethodParameter.JavaSourceFileMethodParametersParser;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.typemapping.JavaImport;

/**
 * 
 * @author badqiu,Linlin Yu
 */
public class JavaMethod {
	Method method;
	private JavaClass clazz; //与method相关联的class
	
	
	public JavaMethod(Method method, JavaClass clazz) {
		super();
		if(method == null) throw new IllegalArgumentException("method must be not null");
		if(clazz == null) throw new IllegalArgumentException("clazz must be not null");
		this.method = method;
		this.clazz = clazz;
	}

	public JavaClass getClazz() {
		return clazz;
	}

	public String getMethodName() {
		return method.getName();
	}

	public JavaClass getReturnType() {
		return new JavaClass(method.getReturnType());
	}
	
	public Annotation[] getAnnotations() {
		return method.getAnnotations();
	}

	public boolean isBridge() {
		return method.isBridge();
	}

    public List<JavaClass> getExceptionTypes() {
        List<JavaClass> result = new ArrayList();
        for(Class c : method.getExceptionTypes()) {
            result.add(new JavaClass(c));
        }
        return result;
    }

    public boolean isSynthetic() {
		return method.isSynthetic();
	}

	public boolean isVarArgs() {
		return method.isVarArgs();
	}

	public Set<JavaClass> getImportClasses() {
		Set<JavaClass> set = new LinkedHashSet<JavaClass>();
        JavaImport.addImportClass(set,method.getParameterTypes());
        JavaImport.addImportClass(set,method.getExceptionTypes());
        JavaImport.addImportClass(set, method.getReturnType());
        return set;
	}
	
	public List<MethodParameter> getParameters() {
		Class[] parameters  = method.getParameterTypes();
		List<MethodParameter> results = new ArrayList<MethodParameter>();
		for(int i = 0; i < parameters.length; i++) {
			results.add(new MethodParameter(i+1,this,new JavaClass(parameters[i])));
		}
		return results;
	}
	
	public String getMethodNameUpper() {
		return StringHelper.capitalize(getMethodName());
	}
	
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JavaMethod other = (JavaMethod) obj;
        if (method == null) {
            if (other.method != null)
                return false;
        } else if (!method.equals(other.method))
            return false;
        return true;
    }
    
    public boolean isPropertyMethod() {
    	if(getMethodName().startsWith("set") || getMethodName().startsWith("get") || (getMethodName().startsWith("is") && getReturnType().isBooleanType())) {
    		return true;
    	}
    	return false;
    }

    public String toString() {
		return clazz.getJavaType()+"."+getMethodName()+"()";
	}
    
    public static class JavaMethodInvokeFlows {
    	public static String fieldMethodInvokeRegex = "(\\w+)\\s*\\.\\s*(\\w+)\\(.*?\\)";
    	
    	Method method;
    	String javaSourceContent;
    	JavaClass clazz;
    	
    	public JavaMethodInvokeFlows(Method method, String javaSourceContent,
				JavaClass clazz) {
			super();
			this.method = method;
			this.javaSourceContent = javaSourceContent;
			this.clazz = clazz;
		}

		public List<JavaMethod> methodInvokeFlows = new ArrayList<JavaMethod>();
    	public void execute() {
    		javaSourceContent = removeJavaComments(javaSourceContent);
    		javaSourceContent = removeImports(javaSourceContent);
    		javaSourceContent = removePackage(javaSourceContent);
    		javaSourceContent = replaceString2EmptyString(javaSourceContent);
    		String methodStartPattern = "(?s)"+method.getName()+"\\s*\\("+JavaSourceFileMethodParametersParser.getParamsPattern(method)+"\\)\\s*";
    		int methodStart = StringHelper.indexOfByRegex(javaSourceContent,methodStartPattern);
    		String methodEnd = javaSourceContent.substring(methodStart);
    		int[] beginAndEnd = findWrapCharEndLocation(methodEnd,'{','}');
    		String methodBody = methodEnd.substring(beginAndEnd[0], beginAndEnd[1]);
    		
    		Pattern p = Pattern.compile(fieldMethodInvokeRegex);
    		Matcher m = p.matcher(methodBody);
    		while(m.find()) {
    			String field = m.group(1);
    			String methodName= m.group(2);
    			try {
					JavaField javaField = clazz.getField(field);
					JavaClass fieldType = javaField.getType();
					JavaMethod method = fieldType.getMethod(methodName);
					if(method != null) {
						methodInvokeFlows.add(method);
					}
				} catch (NoSuchFieldException e) {
					continue;
				}
    		}
    	}
    	public static  String replaceString2EmptyString(String str) {
    		if(str == null) return null;
    		str = str.replaceAll("\".*?\"", ""); // remove "234 " => ""
			return str;
		}
		// getName\s*\(.*?\)\s*\{.*?\;\s*}
		public static String removeJavaComments(String str) {
			if(str == null) return null;
			str = str.replaceAll("//.*", ""); // remove //
			str = str.replaceAll("(?s)/\\*.*?\\*/", ""); // remove /* */
			return str;
		}
		public static String removeImports(String str) {
    		if(str == null) return null;
    		str = str.replaceAll("\\s*import.*", ""); // remove "234 " => ""
			return str;
		}
		public static String removePackage(String str) {
    		if(str == null) return null;
    		str = str.replaceAll("\\s*package.*", ""); // remove "234 " => ""
			return str;
		}
		
		/** 
		 * 找到对称的一条括号所处的位置,
		 * 如 findWrapCharEndLocation("0123{{67}}}",'{','}'), 将返回 [4,9]
		 * 如果没有将到,将返回null 
		 **/
		public static int[] findWrapCharEndLocation(String str,char begin,char end) {
			int count = 0;
			boolean foundEnd = false;
			boolean foundBegin = false;
			int[] beginAndEnd = new int[2];
			for(int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if(c == begin) {
					if(!foundBegin) {
						beginAndEnd[0] = i;
					}
					foundBegin = true;
					count ++;
				}
				if(c == end) {
					foundEnd = true;
					count--;
				}
				if(count == 0 && foundBegin && foundEnd) {
					beginAndEnd[1] = i;
					return beginAndEnd;
				}
			}
			return null;
		}
    }
    
}
