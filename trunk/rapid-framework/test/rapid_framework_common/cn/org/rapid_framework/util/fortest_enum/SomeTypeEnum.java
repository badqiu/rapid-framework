package cn.org.rapid_framework.util.fortest_enum;

import cn.org.rapid_framework.util.KeyValue;
import cn.org.rapid_framework.util.KeyValueUtils;

public enum SomeTypeEnum implements KeyValue<String,String>{
	K1("K1","V1"),
	K2("K2","V2")
	;
	
	private String key;
	private String value;
	SomeTypeEnum(String key,String value) {
		this.key = key;
		this.value = value;
	}
	
	public SomeTypeEnum getByKey(String key) {
		return KeyValueUtils.getByKey(key, values());
	}
	
	public static SomeTypeEnum getByValue(String value) {
		return KeyValueUtils.getByValue(value, values());
	}
	
	public SomeTypeEnum getRequiredByKey(String key) {
		return KeyValueUtils.getRequiredByKey(key, values());
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	
}