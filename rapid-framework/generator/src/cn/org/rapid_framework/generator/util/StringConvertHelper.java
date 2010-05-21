package cn.org.rapid_framework.generator.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class StringConvertHelper {

	/**
	 * 将string转换为Map,string格式为键值对,并以逗号分隔,如:k1="str",k2=30,k3=40
	 */
	public static Map string2Map(String data) {
		if(data == null) return new LinkedHashMap();
		
		Map commands = new LinkedHashMap();
		String[] data_arr = data.split(",");
		for (int i = 0; i < data_arr.length; i++) {
			int equ_pos = data_arr[i].indexOf('=');
			String key = data_arr[i].substring(0, equ_pos);
			String value = data_arr[i].substring(equ_pos + 1);
			commands.put(key, value);
		}
		return commands;
	}
}
