package cn.org.rapid_framework.generator.provider.java.model;

import cn.org.rapid_framework.generator.util.StringHelper;


public class MethodParameter {
	int paramIndex = -1;
	JavaClass paramClazz;
	JavaMethod method;
	
	public MethodParameter(int paramIndex, JavaMethod method,JavaClass paramClazz) {
		super();
		this.paramIndex = paramIndex;
		this.paramClazz = paramClazz;
	}

	public String getName() {
	    if(paramClazz.getClazz().isPrimitive() || paramClazz.getClazz().getName().startsWith("java.")) {
	        return "param"+paramIndex;
	    }else {
	        return StringHelper.uncapitalize(paramClazz.getClassName());
	    }
//		return "param"+paramIndex;
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

	public JavaProperty[] getProperties() throws Exception {
		return paramClazz.getProperties();
	}

	public String getSuperclassName() {
		return paramClazz.getSuperclassName();
	}
	
    public JavaMethod getMethod() {
		return method;
	}

	public JavaClass getParamClazz() {
        return paramClazz;
    }
    
	public String toString() {
		return "MethodParameter:"+getName()+"="+getJavaType();
	}
	
}
