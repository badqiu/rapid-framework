package cn.org.rapid_framework.generator.util;

import java.sql.Timestamp;

/**
 * 
 * @author badqiu
 *
 */
public class TestDataGenerator {

	public String getTestData(String columnName,String javaType, int size) {
		int MAX_SIZE = 3;
		if(javaType.indexOf("Boolean") >= 0) {
			return "0";
		}
		if(javaType.indexOf("Timestamp") >= 0) {
			return new Timestamp(System.currentTimeMillis()).toString();
		}
		if(javaType.indexOf("java.sql.Date") >= 0) {
			return new java.sql.Date(System.currentTimeMillis()).toString();
		}
		if(javaType.indexOf("java.sql.Time") >= 0) {
			return  new java.sql.Time(System.currentTimeMillis()).toString();
		}
		if(javaType.indexOf("String") >= 0) {
			if(size > columnName.length()) {
				int tempSize = Math.min(size - columnName.length(), MAX_SIZE);
				return columnName + StringHelper.randomNumeric(tempSize);
			}
			return StringHelper.randomNumeric(Math.min(size, MAX_SIZE));
		}
		if(isNumberType(javaType)){
			return StringHelper.randomNumeric(Math.min(size, MAX_SIZE));
		}
		return "";
	}

	private boolean isNumberType(String javaType) {
		if(javaType.indexOf("Byte") >= 0 
				|| javaType.indexOf("Short") >= 0 
				|| javaType.indexOf("Integer") >= 0 
				|| javaType.indexOf("Long") >= 0 
				|| javaType.indexOf("Double") >= 0 
				|| javaType.indexOf("BigDecimal") >= 0 
				|| javaType.indexOf("Float") >= 0) {
			return true;
		}
		return false;
	}
	
}
