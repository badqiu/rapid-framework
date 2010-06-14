package cn.org.rapid_framework.generator.provider.db.model.util;

import cn.org.rapid_framework.generator.provider.db.model.Column;
import cn.org.rapid_framework.generator.util.DatabaseDataTypesUtils;

public class ColumnHelper {

	/** 得到JSR303 bean validation的验证表达式 */
	public static String getJSR303Validation(Column c) {
		if(!c.isNullable()) {
			if(DatabaseDataTypesUtils.isString(c.getSqlType(), c.getSize(), c.getDecimalDigits())) {
				return  "@NotBlank " + getNotRequiredValidation(c);
			}else {
				return  "@NotNull " + getNotRequiredValidation(c);
			}
		}else {
			return getNotRequiredValidation(c);
		}
	}

	public static String getNotRequiredValidation(Column c) {
		String result = "";
		if(c.getSqlName().indexOf("mail") >= 0) {
			result += "@Email ";
		}
		if(DatabaseDataTypesUtils.isString(c.getSqlType(), c.getSize(), c.getDecimalDigits())) {
			result += String.format("@Max(%s)",c.getSize());
		}
		if(DatabaseDataTypesUtils.isIntegerNumber(c.getSqlType(), c.getSize(), c.getDecimalDigits())) {
			String javaType = DatabaseDataTypesUtils.getPreferredJavaType(c.getSqlType(), c.getSize(), c.getDecimalDigits());
			if(javaType.toLowerCase().indexOf("short") >= 0) {
				result += " @Max("+Short.MAX_VALUE+")";
			}else if(javaType.toLowerCase().indexOf("byte") >= 0) {
				result += " @Max("+Byte.MAX_VALUE+")";
			}
		}
		return result.trim();
	}
	
	/** 得到rapid validation的验证表达式 */
	public static String getRapidValidation(Column c) {
		String result = "";
		if(c.getSqlName().indexOf("mail") >= 0) {
			result += "validate-email ";
		}
		if(DatabaseDataTypesUtils.isFloatNumber(c.getSqlType(), c.getSize(), c.getDecimalDigits())) {
			result += "validate-number ";
		}
		if(DatabaseDataTypesUtils.isIntegerNumber(c.getSqlType(), c.getSize(), c.getDecimalDigits())) {
			result += "validate-integer ";
			if(c.getJavaType().toLowerCase().indexOf("short") >= 0) {
				result += "max-value-"+Short.MAX_VALUE;
			}else if(c.getJavaType().toLowerCase().indexOf("integer") >= 0) {
				result += "max-value-"+Integer.MAX_VALUE;
			}else if(c.getJavaType().toLowerCase().indexOf("byte") >= 0) {
				result += "max-value-"+Byte.MAX_VALUE;
			}
		}
		return result;
	}
}
