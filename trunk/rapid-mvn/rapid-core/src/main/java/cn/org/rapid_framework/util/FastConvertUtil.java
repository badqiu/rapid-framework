package cn.org.rapid_framework.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.BeanUtils;

public class FastConvertUtil {

	public static Object convert(Class<?> targetClass, Object value) {
		if(value == null) {
			return getDefaultByClass(targetClass);
		}
		if(targetClass == value.getClass()) {
			return value;
		}
		if(targetClass == String.class) {
			return value.toString();
		}
		if(isEqualsClass(targetClass,value.getClass())) {
			return value;
		}
		if(isDateType(targetClass)) {
			Date date = (Date)BeanUtils.instantiate(targetClass);
			if(value instanceof Number) {
				date.setTime(((Number) value).longValue());
			}else if(value instanceof String) {
				date.setTime(Long.parseLong(String.valueOf(value)));
			}else if(value instanceof Date) {
				date.setTime(((Date)value).getTime());
			}else if(value instanceof Calendar) {
				date.setTime(((Calendar)value).getTimeInMillis());
			}else {
				throw new IllegalStateException("cannot convert value:"+value+" to target date:"+targetClass);
			}
			return date;
        }
		
		return convert2FromString(targetClass, String.valueOf(value));
	}

	private static Object convert2FromString(Class<?> targetClass, String value) {
		if(targetClass == int.class || targetClass == Integer.class) {
			return Integer.parseInt(String.valueOf(value));
		}
		if(targetClass == long.class || targetClass == Long.class) {
			return Long.parseLong(String.valueOf(value));
		}
		if(targetClass == double.class || targetClass == Double.class) {
			return Double.parseDouble(String.valueOf(value));
		}
		if(targetClass == float.class || targetClass == Float.class) {
			return Float.parseFloat(String.valueOf(value));
		}
		if(targetClass == char.class || targetClass == Character.class) {
			return value;
		}
		if(targetClass == short.class || targetClass == Short.class) {
			return Short.parseShort(String.valueOf(value));
		}
		if(targetClass == byte.class || targetClass == Byte.class) {
			return Byte.parseByte(String.valueOf(value));
		}
        if(targetClass == BigDecimal.class) {
            return new BigDecimal(value);
        }
        if(targetClass == BigInteger.class) {
            return BigInteger.valueOf(Long.parseLong(value));
        }
		return value;
	}

	private static boolean isEqualsClass(Class<?> source,
			Class<? extends Object> target) {
		if(isEquals(source,target,int.class,Integer.class)) {
			return true;
		}else if(isEquals(source,target,long.class,Long.class)) {
			return true;
		}else if(isEquals(source,target,boolean.class,Boolean.class)) {
			return true;
		}else if(isEquals(source,target,double.class,Double.class)) {
			return true;
		}else if(isEquals(source,target,float.class,Float.class)) {
			return true;
		}else if(isEquals(source,target,byte.class,Byte.class)) {
			return true;
		}else if(isEquals(source,target,short.class,Short.class)) {
			return true;
		}else if(isEquals(source,target,char.class,Character.class)) {
			return true;
		}
		return false;
	}

	private static boolean isEquals(Class<?> source,
			Class<? extends Object> target, Class type1,
			Class type2) {
		if(source == type1 || source == type2) {
			if(target == type1 || target ==type2)
				return true;
		}
		return false;
	}

	private static Object getDefaultByClass(Class<?> targetClass) {
		if(targetClass.isPrimitive()) {
			if (targetClass == int.class) {
				return 0;
			} else if (targetClass == boolean.class) {
				return false;			
			} else if (targetClass == long.class) {
				return 0L;
			} else if (targetClass == double.class) {
				return (double) 0;
			} else if (targetClass == float.class) {
				return (float) 0;
			} else if (targetClass == char.class) {
				return (char) 0;
			} else if (targetClass == short.class) {
				return (short) 0;
			} else if (targetClass == byte.class) {
				return (byte) 0;
			}
		}
		return null;
	}
	
    public static boolean isDateType(Class<?> targetType) {
        if(targetType == null) return false;
        return targetType == java.util.Date.class || targetType == java.sql.Timestamp.class || targetType == java.sql.Date.class || targetType == java.sql.Time.class;
    }
}
