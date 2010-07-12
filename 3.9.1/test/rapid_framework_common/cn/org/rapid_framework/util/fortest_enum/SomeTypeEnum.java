package cn.org.rapid_framework.util.fortest_enum;

import cn.org.rapid_framework.lang.enums.EnumBase;
import cn.org.rapid_framework.lang.enums.EnumBaseUtils;

public enum SomeTypeEnum implements EnumBase<String,String>{
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
		return EnumBaseUtils.getByCode(key, values());
	}
	
	public SomeTypeEnum getRequiredByKey(String key) {
		return EnumBaseUtils.getRequiredByCode(key, values());
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public String getCode() {
		return key;
	}

	public String getDesc() {
		return value;
	}
	
	
}