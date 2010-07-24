package cn.org.rapid_framework.beanutils.converter;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import cn.org.rapid_framework.lang.enums.EnumBase;
import cn.org.rapid_framework.lang.enums.EnumBaseUtils;

public class EnumBaseConverter implements Converter {

	public Object convert(Class type, Object value) {
		if(value == null) return null;
		
		if(EnumBase.class.isAssignableFrom(type)) {
        	return convert2EnumBase(type,value);
        }else {
        	return convertEnumBase2TargetType(type, value);
        }
	}

	private Object convertEnumBase2TargetType(Class type, Object value) {
		if(value == null) return null;
		
		EnumBase enumBase = (EnumBase)value;
		if(String.class.isAssignableFrom(type)) {
			return enumBase.getCode().toString();
		}
		
		if(!type.isInstance(value)) {
			throw new ConversionException("enum type mission match,targetType:"+type+" expected type:"+enumBase.getCode().getClass().getName());
		}
		
		try {
			return enumBase.getCode();
		}catch(Exception e) {
			throw new ConversionException("cannot convert value:"+value+" to targetType:"+type.getName(),e);
		}
	}

	private EnumBase convert2EnumBase(Class type, Object value) {
		if(value instanceof EnumBase) {
			return (EnumBase)value;
		}
		if(value instanceof String) {
			String str = (String)value;
			if(str.length() == 0) {
				return null;
			}
		}
		
		EnumBase[] values = (EnumBase[])type.getEnumConstants();
		for(EnumBase enumBase : values) {
			if(enumBase.getCode().equals(value)) {
				return enumBase;
			}
		}
		throw new ConversionException("cannot convert value:"+value+" to targetType:"+type.getName());
	}

}
