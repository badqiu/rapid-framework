package cn.org.rapid_framework.web.enums;

/**
 * 用于enum或是对象继承,以便将enum转换为用于构建form表单中的select input值
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
 *	public String getFormInputKey() {
 *		return name();
 *	}
 *
 *	public String getFormInputLabel() {
 *		return label;
 *	}
 *
 *	}
 * </pre>
 * @author badqiu
 *
 */
public interface FormInputEnum {
	
	public String getFormInputKey();
	
	public String getFormInputLabel();
	
}
