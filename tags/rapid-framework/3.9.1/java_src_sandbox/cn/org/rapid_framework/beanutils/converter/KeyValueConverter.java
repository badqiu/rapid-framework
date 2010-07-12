package cn.org.rapid_framework.beanutils.converter;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.collections.KeyValue;

public class KeyValueConverter implements Converter {

    public KeyValueConverter() {
    }

    public Object convert(Class type, Object value) {
        if(value == null) return null;
        if(type.isAssignableFrom(KeyValue.class)) {
        	return convert2KeyValue(value);
        }else {
        	return convertKeyValue2TargetType(type, value);
        }
    }

    private Object convert2KeyValue(Object value) {
    	throw new IllegalArgumentException("not yet implement");
	}

	private Object convertKeyValue2TargetType(Class type, Object value) {
        KeyValue kv = (KeyValue)value;
        if(kv.getKey() == null) return null;
        
        if(type == String.class) {
            return kv.getKey().toString();
        }else if(type == Integer.class) {
            return new Integer(kv.getKey().toString());
        }else if(type == Long.class) {
            return new Long(kv.getKey().toString());
        }else if(type == Short.class) {
            return new Short(kv.getKey().toString());
        }else if(type == Byte.class) {
            return new Byte(kv.getKey().toString());
        }else {
            throw new ConversionException("");
        }
    }
    
}