package cn.org.rapid_framework.generator.provider.java.model;

import java.lang.reflect.Field;

import cn.org.rapid_framework.generator.util.ActionScriptDataTypesUtils;

public class JavaField {
	private Field field;
	private JavaClass clazz;
	
	public JavaField(Field field, JavaClass clazz) {
		super();
		this.field = field;
		this.clazz = clazz;
	}

	public String getFieldName() {
		return field.getName();
	}

	public JavaClass getClazz() {
		return clazz;
	}

	public String getJavaType() {
		return field.getType().getName();
	}

	public String getAsType() {
		return ActionScriptDataTypesUtils.getPreferredAsType(getJavaType());
	}

	public boolean getIsDateTimeField() {
		return  getJavaType().equalsIgnoreCase("java.util.Date")
				|| getJavaType().equalsIgnoreCase("java.sql.Date")
				|| getJavaType().equalsIgnoreCase("java.sql.Timestamp")
				|| getJavaType().equalsIgnoreCase("java.sql.Time");
	}
	
	public String toString() {
		return "JavaClass:"+clazz+" JavaField:"+getFieldName();
	}
}
