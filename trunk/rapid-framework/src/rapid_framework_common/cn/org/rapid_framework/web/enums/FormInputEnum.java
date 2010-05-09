package cn.org.rapid_framework.web.enums;
/**
 * 用于构建得到form表单中的select input值
 * 
 * <pre>
 * public enum RapidAreaEnum implements FormInputEnum {
 *	GD("广东"),SH("上海");
 *	
 *	public String label;
 *	RapidAreaEnum(String v) {
 *		this.label = v;
 *	}
 *	
 *	public String getInputKey() {
 *		return name();
 *	}
 *
 *	public String getDisplayLabel() {
 *		return label;
 *	}
 *
 *	}
 * </pre>
 * @author badqiu
 *
 */
public interface FormInputEnum {
	
	public String getInputKey();
	
	public String getDisplayLabel();
	
}
