package cn.org.rapid_framework.generator.util.paranamer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.StringHelper;
/**
 * get parameter names from java source file
 * @author badqiu
 */
public class JavaSourceParanamer implements Paranamer{
    private ClassLoader classLoader;
    
    public JavaSourceParanamer(ClassLoader classLoader) {
    	this.classLoader = classLoader;
    }
    
    public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
        return lookupParameterNames(methodOrConstructor,true);
    }

    @SuppressWarnings("unchecked")
	public String[] lookupParameterNames(AccessibleObject methodOrConstructor,boolean throwExceptionIfMissing) {
        try {
	        JavaSourceFileMethodParametersParser parser = new JavaSourceFileMethodParametersParser();
	        if(methodOrConstructor instanceof Method) {
	        	Method m = (Method)methodOrConstructor;
	        	InputStream javaSourceInput = classLoader.getResourceAsStream(m.getDeclaringClass().getName().replace('.', '/')+".java");
	            return parser.parseJavaFileForParamNames(m, IOHelper.toString(javaSourceInput)); 
	        }else if(methodOrConstructor instanceof Constructor) {
	        	Constructor c = (Constructor)methodOrConstructor;
	        	InputStream javaSourceInput = classLoader.getResourceAsStream(c.getDeclaringClass().getName().replace('.', '/')+".java");
	            return parser.parseJavaFileForParamNames(c, IOHelper.toString(javaSourceInput));
	        }else {
	            throw new IllegalArgumentException("unknow AccessibleObject"+methodOrConstructor+",must be Method or Constructor");
	        }
        }catch(IOException e) {
        	if(throwExceptionIfMissing) {
        		throw new RuntimeException("cannot get method parameters:"+methodOrConstructor,e);
        	}else {
        		return null;
        	}
        }
    }
    
    public static class JavaSourceFileMethodParametersParser {

        public String[] parseJavaFileForParamNames(Constructor<?> constructor,String content) {
            return parseJavaFileForParamNames(content,constructor.getName(),constructor.getParameterTypes());
        }
        
        public String[] parseJavaFileForParamNames(Method method,String content) {
            return parseJavaFileForParamNames(content, method.getName(), method.getParameterTypes());
        }

        private String[] parseJavaFileForParamNames(String content,
                                                    String name,
                                                    Class<?>[] parameterTypes) {
            Pattern methodPattern = Pattern.compile("(?s)"+name+"\\s*\\("+getParamsPattern(parameterTypes)+"\\)\\s*\\{");
            Matcher m = methodPattern.matcher(content);
            List<String> paramNames = new ArrayList<String>();
            while(m.find()) {
                for(int i = 1; i <= parameterTypes.length; i++) {
                    paramNames.add(m.group(i));
                }
                return (String[])paramNames.toArray(new String[0]);
            }
            return null;
        }

        private String getParamsPattern(Class<?>[] parameterTypes) {
            List paramPatterns = new ArrayList();
            for(int i = 0; i < parameterTypes.length; i++ ) {
                Class type = parameterTypes[i];
                String paramPattern = ".*"+type.getSimpleName()+".*\\s+(\\w+).*";
                paramPatterns.add(paramPattern);
            }
            return StringHelper.join(paramPatterns, ",");
        }
        
    }

}
