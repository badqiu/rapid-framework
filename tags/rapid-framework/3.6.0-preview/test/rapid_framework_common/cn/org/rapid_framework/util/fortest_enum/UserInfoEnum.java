
package cn.org.rapid_framework.util.fortest_enum;
import java.util.Map;

import cn.org.rapid_framework.util.KeyValue;
import cn.org.rapid_framework.util.KeyValueUtils;

public class UserInfoEnum {

	public static enum UsernameEnum implements KeyValue<String,String>{
		K1("K1","V1"),
		K2("K2","V2")
		;
		
		private final String key;
		private final String value;
		UsernameEnum(String key,String value) {
			this.key = key;
			this.value = value;
		}
		
		public static UsernameEnum getByKey(String key) {
			return KeyValueUtils.getByKey(key, values());
		}
		
		public static UsernameEnum getByValue(String value) {
			return KeyValueUtils.getByValue(value, values());
		}
		
		public static UsernameEnum getRequiredByKey(String key) {
			return KeyValueUtils.getRequiredByKey(key, values());
		}
		
		public String getKey() {
			return key;
		}
	
		public String getValue() {
			return value;
		}
		
	}
	public static enum PasswordEnum implements KeyValue<String,String>{
		K1("K1","V1"),
		K2("K2","V2")
		;
		
		private final String key;
		private final String value;
		PasswordEnum(String key,String value) {
			this.key = key;
			this.value = value;
			
			Map<String,String> map = KeyValueUtils.toMap(values());
		}
		
		public static PasswordEnum getByKey(String key) {
			return KeyValueUtils.getByKey(key, values());
		}
		
		public static PasswordEnum getByValue(String value) {
			return KeyValueUtils.getByValue(value, values());
		}
		
		public static PasswordEnum getRequiredByKey(String key) {
			return KeyValueUtils.getRequiredByKey(key, values());
		}
		
		public String getKey() {
			return key;
		}
	
		public String getValue() {
			return value;
		}
		
	}
	public static enum BirthDateEnum implements KeyValue<String,String>{
		K1("K1","V1"),
		K2("K2","V2")
		;
		
		private final String key;
		private final String value;
		BirthDateEnum(String key,String value) {
			this.key = key;
			this.value = value;
		}
		
		public static BirthDateEnum getByKey(String key) {
			return KeyValueUtils.getByKey(key, values());
		}
		
		public static BirthDateEnum getByValue(String value) {
			return KeyValueUtils.getByValue(value, values());
		}
		
		public static BirthDateEnum getRequiredByKey(String key) {
			return KeyValueUtils.getRequiredByKey(key, values());
		}
		
		public String getKey() {
			return key;
		}
	
		public String getValue() {
			return value;
		}
		
	}
	public static enum SexEnum implements KeyValue<String,String>{
		K1("K1","V1"),
		K2("K2","V2")
		;
		
		private final String key;
		private final String value;
		SexEnum(String key,String value) {
			this.key = key;
			this.value = value;
		}
		
		public static SexEnum getByKey(String key) {
			return KeyValueUtils.getByKey(key, values());
		}
		
		public static SexEnum getByValue(String value) {
			return KeyValueUtils.getByValue(value, values());
		}
		
		public static SexEnum getRequiredByKey(String key) {
			return KeyValueUtils.getRequiredByKey(key, values());
		}
		
		public String getKey() {
			return key;
		}
	
		public String getValue() {
			return value;
		}
		
	}
	public static enum AgeEnum implements KeyValue<String,String>{
		K1("K1","V1"),
		K2("K2","V2")
		;
		
		private final String key;
		private final String value;
		AgeEnum(String key,String value) {
			this.key = key;
			this.value = value;
		}
		
		public static AgeEnum getByKey(String key) {
			return KeyValueUtils.getByKey(key, values());
		}
		
		public static AgeEnum getByValue(String value) {
			return KeyValueUtils.getByValue(value, values());
		}
		
		public static AgeEnum getRequiredByKey(String key) {
			return KeyValueUtils.getRequiredByKey(key, values());
		}
		
		public String getKey() {
			return key;
		}
	
		public String getValue() {
			return value;
		}
		
	}
	public static enum UserIdEnum implements KeyValue<String,String>{
		K1("K1","V1"),
		K2("K2","V2")
		;
		
		private final String key;
		private final String value;
		UserIdEnum(String key,String value) {
			this.key = key;
			this.value = value;
		}
		
		public static UserIdEnum getByKey(String key) {
			return KeyValueUtils.getByKey(key, values());
		}
		
		public static UserIdEnum getByValue(String value) {
			return KeyValueUtils.getByValue(value, values());
		}
		
		public static UserIdEnum getRequiredByKey(String key) {
			return KeyValueUtils.getRequiredByKey(key, values());
		}
		
		public String getKey() {
			return key;
		}
	
		public String getValue() {
			return value;
		}
		
	}

}

