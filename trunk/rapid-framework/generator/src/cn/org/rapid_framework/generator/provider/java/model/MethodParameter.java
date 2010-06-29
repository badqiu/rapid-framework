package cn.org.rapid_framework.generator.provider.java.model;

import java.util.List;

import cn.org.rapid_framework.generator.util.StringHelper;


public class MethodParameter {
	int paramIndex = -1;
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
    
	public String toString() {
		return "MethodParameter:"+getName()+"="+getJavaType();
	}
	
}
