package cn.org.rapid_framework.generator.util.paranamer;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.rapid_framework.generator.util.StringHelper;
/**
 * get parameter names from java source file
 * @author badqiu
 */
public class JavaSourceParanamer implements Paranamer{
    
    public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
        return lookupParameterNames(methodOrConstructor,true);
    }

    public String[] lookupParameterNames(AccessibleObject methodOrConstructor,boolean throwExceptionIfMissing) {
        if(true) throw new IllegalArgumentException("not yet implements");
        JavaSourceFileMethodParametersParser parser = new JavaSourceFileMethodParametersParser();
        if(methodOrConstructor instanceof Method) {
            return parser.parseJavaFileForParamNames((Method)methodOrConstructor, null); 
        }else if(methodOrConstructor instanceof Constructor) {
            return parser.parseJavaFileForParamNames((Constructor)methodOrConstructor, null);
        }else {
            throw new IllegalArgumentException("unknow AccessibleObject"+methodOrConstructor+",must be Method or Constructor");
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
