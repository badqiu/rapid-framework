package cn.org.rapid_framework.web.enums;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 将一组FormInputEnum[]转变为map, key为FormInputEnum.getInputKey(), value为FormInputEnum.getDisplayLabel()
 * @author badqiu
 *
 */
public class FormInputEnumUtils {
	
	public static LinkedHashMap toMap(FormInputEnum[] values) {
		if(values == null) return new LinkedHashMap();
		LinkedHashMap map = new LinkedHashMap();
		for(FormInputEnum v : values) {
			map.put(v.getInputKey(), v.getDisplayLabel());
		}
		return map;
	}
	
}
