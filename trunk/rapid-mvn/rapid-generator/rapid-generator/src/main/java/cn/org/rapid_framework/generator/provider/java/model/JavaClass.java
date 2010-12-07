package cn.org.rapid_framework.generator.provider.java.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.org.rapid_framework.generator.provider.java.model.JavaMethod.FieldMethodInvocation;
import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.typemapping.ActionScriptDataTypesUtils;
import cn.org.rapid_framework.generator.util.typemapping.JavaImport;
import cn.org.rapid_framework.generator.util.typemapping.JavaPrimitiveTypeMapping;

public class JavaClass {
	private Class clazz;
	public JavaClass(Class clazz) {
		this.clazz = clazz;
	}
	
	public String getClassName() {
		return getClassName(getSimpleJavaType());
	}

    static String getClassName(String simpleJavaType) {
        return simpleJavaType.indexOf('$') >= 0 ? simpleJavaType.substring(simpleJavaType.indexOf('$')+1) : simpleJavaType;
    }
	
	public String getPackageName() {
		return clazz.getPackage().getName();
	}
	
	public String getLastPackageName() {
		return StringHelper.getExtension(getPackageName());
	}

	public String getLastPackageNameFirstUpper() {
		return getLastPackageName() == null ? "" : StringHelper.capitalize(getLastPackageName());
	}
	
	public boolean isHasDefaultConstructor() {
	    if(clazz.isInterface() || clazz.isAnnotation() || clazz.isEnum() || Modifier.isAbstract(clazz.getModifiers()))
	        return false;
	    for(Constructor c : clazz.getConstructors()) {
	        if(Modifier.isPublic(c.getModifiers())) {
	            if(c.getParameterTypes().length == 0) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
	
	public Set<JavaClass> getImportClasses() {
	    Set<JavaClass> set = new LinkedHashSet<JavaClass>();
	    for(Method m :clazz.getMethods()) {
	        Class[] clazzes = { m.getReturnType() };
            JavaImport.addImportClass(set,clazzes);
	        JavaImport.addImportClass(set,m.getParameterTypes());
	        JavaImport.addImportClass(set,m.getExceptionTypes());
	    }
	    if(clazz.isMemberClass()) {
	        Class[] clazzes = { clazz };
            JavaImport.addImportClass(set,clazzes);
	    }
	    for(Field f :clazz.getFields()) {
            Class[] clazzes = { f.getType() };
            JavaImport.addImportClass(set,clazzes);
        }
	    for(Field f :clazz.getDeclaredFields()) {
            Class[] clazzes = { f.getType() };
            JavaImport.addImportClass(set,clazzes);
        }
	    for(Constructor c : clazz.getDeclaredConstructors()) {
	    	JavaImport.addImportClass(set,c.getExceptionTypes());
	    	JavaImport.addImportClass(set,c.getParameterTypes());
	    }
	    for(Constructor c : clazz.getConstructors()) {
            JavaImport.addImportClass(set,c.getExceptionTypes());
            JavaImport.addImportClass(set,c.getParameterTypes());
        }
	    for(Class c : clazz.getDeclaredClasses()) {
            JavaImport.addImportClass(set,c);
        }
	    return set;
	}

   public Set<JavaClass> getPropertiesImportClasses() throws Exception {
       Set<JavaClass> set = getImportClasses();
       for(JavaProperty prop : getProperties()) {
           set.addAll(prop.getPropertyType().getImportClasses());
       }
       return set;
   }
	
    public String getSuperclassName() {
		return clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : null;
	}
	
	public JavaMethod[] getMethods() {
		return toJavaMethods(clazz.getDeclaredMethods());
	}
	
	public JavaMethod getMethod(String methodName) {
		for(Method m : clazz.getMethods()) {
			if(m.getName().equals(methodName)) {
				return new JavaMethod(m,this); //FIXME javaClazz关联错误,现在会覆盖父类的引用.
			}
		}
		return null;
	}
	
	public JavaMethod[] getPublicMethods() {
		Method[] methods = clazz.getDeclaredMethods();
		return toJavaMethods(filterByModifiers(methods,Modifier.PUBLIC));
	}

	public JavaMethod[] getPublicStaticMethods() {
		Method[] methods = clazz.getDeclaredMethods();
		return toJavaMethods(filterByModifiers(methods,Modifier.PUBLIC,Modifier.STATIC));
	}
	
	public JavaMethod[] getPublicNotStaticMethods() {
		Method[] staticMethods = filterByModifiers(clazz.getDeclaredMethods(),Modifier.STATIC);
		Method[] publicMethods = filterByModifiers(clazz.getDeclaredMethods(),Modifier.PUBLIC);
		Method[] filtered = exclude(publicMethods,staticMethods).toArray(new Method[0]);
		return toJavaMethods(filtered);
	}

	public JavaProperty[] getReadProperties() throws Exception {
		List result = new ArrayList();
		for(JavaProperty p : getProperties()) {
			if(p.isHasReadMethod()) {
				result.add(p);
			}
		}
		return (JavaProperty[])result.toArray(new JavaProperty[0]);
	}

	public JavaProperty[] getWriteProperties() throws Exception {
		List result = new ArrayList();
		for(JavaProperty p : getProperties()) {
			if(p.isHasWriteMethod()) {
				result.add(p);
			}
		}
		return (JavaProperty[])result.toArray(new JavaProperty[0]);
	}
	
	public JavaProperty[] getProperties() throws Exception {
		List<JavaProperty> result = new ArrayList<JavaProperty>();
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor pd : pds) {
		    if(!"class".equalsIgnoreCase(pd.getName())) {
		        result.add(new JavaProperty(pd,this));
		    }
		}
		return (JavaProperty[])result.toArray(new JavaProperty[0]);
	}
	
	public List<JavaField> getFields() {
		Field[] fields = clazz.getDeclaredFields();
		List result = new ArrayList();
		for(Field f : fields) {
			result.add(new JavaField(f,this));
		}
		return result;
	}
	
	/**
	 * 得到当前类包名称对应的文件路径,如 com.company.project包,将返回 com/company/project
	 * */
	public String getPackagePath(){
		return getPackageName().replace(".", "/");
	}
	
	/**
	 * 得到当前类父一级的包名称如 com.company.project包,将返回com.company
	 * */
	public String getParentPackageName() {
		return getPackageName().substring(0,getPackageName().lastIndexOf("."));
	}
	/**
	 * 得到当前类父一级的包名称对应的文件路径,如 com.company.project包,将返回com/company
	 * */
	public String getParentPackagePath() {
		return getParentPackageName().replace(".", "/");
	}
	
	/**
	 * 得到当前类是从那里装载的绝对路径
	 * */
	public String getClassFile() {
	    return clazz.getClassLoader().getResource(clazz.getName().replace('.', '/')+".class").getFile();
	}
	/**
	 * 得到当前类对应的java源文件名称
	 * */
	public String getJavaSourceFile() {
        return clazz.getName().replace('.', '/')+".java";
	}

	/**
	 * 得到 当前类对应的测试代码的文件路径
	 * @return
	 */
	public String getMavenJavaTestSourceFile() {
	    return MavenHelper.getMavenJavaTestSourceFile(getClassFile());
    }

	/**
	 * 得到 当前类对应的源代码的文件路径
	 * @return
	 */
	public String getMavenJavaSourceFile() {
	    return MavenHelper.getMavenJavaSourceFile(getClassFile());
    }

	/**
	 * 得到 当前类的源代码内容
	 * @return
	 */
	private String mavenJavaSourceFileContent;
	public String getMavenJavaSourceFileContent() {
		if(mavenJavaSourceFileContent == null){
			if(getMavenJavaSourceFile() != null) {
				File file = new File(getMavenJavaSourceFile());
				if(file.exists()) {
				    mavenJavaSourceFileContent = IOHelper.readFile(file);
				}
				//TODO 增加读取 maven app-sources.jar只的源代码文件地址.
			}
		}
		return mavenJavaSourceFileContent;
    }
	
	public static class MavenHelper {
	    public static String getMavenJavaTestSourceFile(String clazzFile) {
	        if(clazzFile == null) return null;
	        clazzFile = clazzFile.replace('\\', '/');
	        clazzFile = clazzFile.replaceAll("\\w+\\$", ""); // for inner class User$1.class and User$Info.class
	        if(clazzFile.indexOf("target/classes") >= 0) {
	            String result = StringHelper.replace(clazzFile, "target/classes", "src/test/java");
	    	    return StringHelper.replace(result, ".class", "Test.java");
	        } else if (clazzFile.indexOf("target/test-classes") >= 0){
	        	String result = StringHelper.replace(clazzFile, "target/test-classes", "src/test/java");
	        	return StringHelper.replace(result, ".class", "Test.java");
	        }else {
	            return null;
	        }
	    }
	
	    public static String getMavenJavaSourceFile(String clazzFile) {
	        if(clazzFile == null) return null;
	        clazzFile = clazzFile.replace('\\', '/');
	        clazzFile = clazzFile.replaceAll("\\$\\w+", ""); // for inner class User$1.class and User$Info.class
	        if(clazzFile.indexOf("target/classes") >= 0) {
	            String result = StringHelper.replace(clazzFile, "target/classes", "src/main/java");
	    	    return StringHelper.replace(result, ".class", ".java");
	        } else if (clazzFile.indexOf("target/test-classes") >= 0){
	        	String result = StringHelper.replace(clazzFile, "target/test-classes", "src/test/java");
	    	    return StringHelper.replace(result, ".class", ".java");
	        }
	        else {
	            return null;
	        }
	    }
	}
    
	/**
	 * 得到class是在那个classpath路径装载
	 * @return
	 */
    public String getLoadedClasspath() {
        return getClassFile().substring(0,getClassFile().length() - (clazz.getName()+".class").length());
    }
	
	public String getAsType() {
		return ActionScriptDataTypesUtils.getPreferredAsType(getJavaType());
	}
	
	public String getJavaType() {
	    if(isArray()) {
	        return clazz.getComponentType().getName().replace("$", ".");
	    }else {
	        return clazz.getName().replace("$", ".");
	    }
	}
	

	public String getPrimitiveJavaType() {
	    return JavaPrimitiveTypeMapping.getPrimitiveType(getJavaType());
	}
	
	public String getSimpleJavaType() {
	    if(isArray()) {
            return clazz.getComponentType().getSimpleName();
        }else {
            return clazz.getSimpleName();
        }
	}
	
	public String getNullValue () {
	    return JavaPrimitiveTypeMapping.getDefaultValue(getJavaType());
	}
	
	public String getCanonicalName() {
		return clazz.getCanonicalName();
	}

	public JavaField getField(String name) throws NoSuchFieldException,SecurityException {
		return new JavaField(clazz.getDeclaredField(name),this);
	}

	public JavaClass getSuperclass() {
		return new JavaClass(clazz.getSuperclass());
	}

	public boolean isAnnotation() {
		return clazz.isAnnotation();
	}

	public boolean isAnonymousClass() {
		return clazz.isAnonymousClass();
	}

	public boolean isArray() {
		return clazz.isArray();
	}
	
	public boolean isBooleanType() {
	    return "boolean".equals(clazz.getName()) || "Boolean".equals(clazz.getSimpleName());
	}

	public boolean isEnum() {
		return clazz.isEnum();
	}

	public boolean isInstance(Object obj) {
		return clazz.isInstance(obj);
	}

	public boolean isInterface() {
		return clazz.isInterface();
	}

	public boolean isLocalClass() {
		return clazz.isLocalClass();
	}

	public boolean isMemberClass() {
		return clazz.isMemberClass();
	}

	public boolean isPrimitive() {
		return clazz.isPrimitive();
	}

	public boolean isSynthetic() {
		return clazz.isSynthetic();
	}

	public Class getClazz() {
	    return clazz;
	}
	
	public Set<FieldMethodInvocation> getFieldMethodInvocationSequences() {
	    Set<FieldMethodInvocation> set = new LinkedHashSet<FieldMethodInvocation>();
	    for(JavaMethod m : getMethods()) {
	        set.addAll(m.getFieldMethodInvocationSequences());
	    }
	    return set;
	}
	
	private Method[] filterByModifiers(Method[] methods,int... filteredModifiers) {
		List<Method> filtered = new ArrayList<Method>();
		for(int i = 0; i < methods.length; i++) {
			for(int j = 0; j < filteredModifiers.length; j++) {
				if((filteredModifiers[j] & methods[i].getModifiers()) != 0) {
					filtered.add(methods[i]);
				}
			}
		}
		return filtered.toArray(new Method[0]);
	}
	
	private JavaMethod[] toJavaMethods(Method[] declaredMethods) {
		JavaMethod[] methods = new JavaMethod[declaredMethods.length];
		for(int i = 0; i < declaredMethods.length; i++) {
			methods[i] = new JavaMethod(declaredMethods[i],this);
		}
		return methods;
	}
	
	private <T> List<T> exclude(T[] methods, T[] excludeMethods) {
		List<T> result = new ArrayList<T>();
		outerLoop:
		for(int i = 0; i < methods.length; i++) {
			for(int j = 0;j < excludeMethods.length; j++) {
				if(methods[i].equals(excludeMethods[j])) {
					break outerLoop;
				}
			}
			result.add(methods[i]);
		}
		return result;
	}
	
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JavaClass other = (JavaClass) obj;
        if (clazz == null) {
            if (other.clazz != null)
                return false;
        } else if (!clazz.equals(other.clazz))
            return false;
        return true;
    }

    public String toString() {
		return clazz.getName();
	}
}
