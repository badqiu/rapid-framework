package cn.org.rapid_framework.generator.provider.java.model;

import java.util.List;

import cn.org.rapid_framework.generator.util.StringHelper;


public class MethodParameter {
	int paramIndex = -1;
	JavaClass paramClazz; //parameter的类型
	JavaMethod method; //与parameter相关联的method
	
	public MethodParameter(int paramIndex, JavaMethod method,JavaClass paramClazz) {
		super();
		this.paramIndex = paramIndex;
		this.paramClazz = paramClazz;
	}

    public JavaMethod getMethod() {
		return method;
	}
    
	public String getName() {
		//return "param"+paramIndex;
	    if(paramClazz.getClazz().isPrimitive() || paramClazz.getClazz().getName().startsWith("java.")) {
	        return "param"+paramIndex;
	    }else {
	        return StringHelper.uncapitalize(paramClazz.getClassName());
	    }
	}

	public String getAsType() {
		return paramClazz.getAsType();
	}

	public String getClassName() {
		return paramClazz.getClassName();
	}

	public String getJavaType() {
		return paramClazz.getJavaType();
	}

	public String getPackageName() {
		return paramClazz.getPackageName();
	}

	public String getPackagePath() {
		return paramClazz.getPackagePath();
	}

	public String getParentPackageName() {
		return paramClazz.getParentPackageName();
	}

	public String getParentPackagePath() {
		return paramClazz.getParentPackagePath();
	}

    public boolean isArray() {
    	return paramClazz.isArray();
    }
    
    
    
	public String getCanonicalName() {
		return paramClazz.getCanonicalName();
	}

	public List<JavaField> getFields() {
		return paramClazz.getFields();
	}

	public JavaMethod[] getMethods() {
		return paramClazz.getMethods();
	}

	public boolean isAnnotation() {
		return paramClazz.isAnnotation();
	}

	public boolean isAnonymousClass() {
		return paramClazz.isAnonymousClass();
	}

	public boolean isEnum() {
		return paramClazz.isEnum();
	}

	public boolean isInterface() {
		return paramClazz.isInterface();
	}

	public boolean isLocalClass() {
		return paramClazz.isLocalClass();
	}

	public boolean isMemberClass() {
		return paramClazz.isMemberClass();
	}

	public boolean isPrimitive() {
		return paramClazz.isPrimitive();
	}

	public boolean isSynthetic() {
		return paramClazz.isSynthetic();
	}

	public JavaProperty[] getProperties() throws Exception {
		return paramClazz.getProperties();
	}

	public String getSuperclassName() {
		return paramClazz.getSuperclassName();
	}

	public JavaClass getParamClazz() {
        return paramClazz;
    }
    
	public String toString() {
		return "MethodParameter:"+getName()+"="+getJavaType();
	}
	
}
