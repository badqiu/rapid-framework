package cn.org.rapid_framework.generator.provider.java.model;

import java.util.List;

import cn.org.rapid_framework.generator.util.StringHelper;


public class MethodParameter {
	int paramIndex = -1;
	JavaClass parameterClass; //parameter的类型
	JavaMethod method; //与parameter相关联的method
	
	public MethodParameter(int paramIndex, JavaMethod method,JavaClass paramClazz) {
		super();
		this.paramIndex = paramIndex;
		this.parameterClass = paramClazz;
	}

    public JavaMethod getMethod() {
		return method;
	}
    
	public String getName() {
		//return "param"+paramIndex;
	    if(parameterClass.getClazz().isPrimitive() || parameterClass.getClazz().getName().startsWith("java.")) {
	        return "param"+paramIndex;
	    }else {
	        return StringHelper.uncapitalize(parameterClass.getClassName());
	    }
	}

	public String getAsType() {
		return parameterClass.getAsType();
	}

	public String getClassName() {
		return parameterClass.getClassName();
	}

	public String getJavaType() {
		return parameterClass.getJavaType();
	}

	public String getPackageName() {
		return parameterClass.getPackageName();
	}

	public String getPackagePath() {
		return parameterClass.getPackagePath();
	}

	public String getParentPackageName() {
		return parameterClass.getParentPackageName();
	}

	public String getParentPackagePath() {
		return parameterClass.getParentPackagePath();
	}

    public boolean isArray() {
    	return parameterClass.isArray();
    }
    
    
    
	public String getCanonicalName() {
		return parameterClass.getCanonicalName();
	}

	public List<JavaField> getFields() {
		return parameterClass.getFields();
	}

	public JavaMethod[] getMethods() {
		return parameterClass.getMethods();
	}

	public boolean isAnnotation() {
		return parameterClass.isAnnotation();
	}

	public boolean isAnonymousClass() {
		return parameterClass.isAnonymousClass();
	}

	public boolean isEnum() {
		return parameterClass.isEnum();
	}

	public boolean isInterface() {
		return parameterClass.isInterface();
	}

	public boolean isLocalClass() {
		return parameterClass.isLocalClass();
	}

	public boolean isMemberClass() {
		return parameterClass.isMemberClass();
	}

	public boolean isPrimitive() {
		return parameterClass.isPrimitive();
	}

	public boolean isSynthetic() {
		return parameterClass.isSynthetic();
	}

	public JavaProperty[] getProperties() throws Exception {
		return parameterClass.getProperties();
	}

	public String getSuperclassName() {
		return parameterClass.getSuperclassName();
	}

	public JavaClass getParameterClass() {
        return parameterClass;
    }
    
	public String toString() {
		return "MethodParameter:"+getName()+"="+getJavaType();
	}
	
}
