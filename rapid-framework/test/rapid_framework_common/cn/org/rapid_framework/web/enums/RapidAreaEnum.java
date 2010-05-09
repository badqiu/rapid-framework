package cn.org.rapid_framework.web.enums;

public enum RapidAreaEnum implements FormInputEnum {
	GD("广东"),SH("上海");
	
	public String label;
	RapidAreaEnum(String v) {
		this.label = v;
	}
	
	public String getInputKey() {
		return name();
	}

	public String getDisplayLabel() {
		return label;
	}

}
