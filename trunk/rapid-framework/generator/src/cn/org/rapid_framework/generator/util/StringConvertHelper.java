package cn.org.rapid_framework.generator.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.rapid_framework.generator.provider.db.model.Column.EnumMedatada;

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
			if(equ_pos == -1) continue;
			String key = data_arr[i].substring(0, equ_pos);
			String value = data_arr[i].substring(equ_pos + 1);
			commands.put(key, value);
		}
		return commands;
	}
	
	/**
	 * 将string转换为List<ColumnEnum>,每个枚举使用逗号分隔: 格式为: "enumAlias(enumKey,enumDesc)"
	 */
	static Pattern rex = Pattern.compile("(.*)\\((.*),(.*)\\)");
	public static List<EnumMedatada> string2ColumnEnumList(String data) {
		if(data == null || data.trim().isEmpty()) return new ArrayList();
		//enumAlias(enumKey,enumDesc),enumAlias(enumDesc)
		
		List<EnumMedatada> list = new ArrayList();
		String[] data_arr = data.split(";");
		for (int i = 0; i < data_arr.length; i++) {
			Matcher m = rex.matcher(data_arr[i]);
			if(m.find()) {
				list.add(new EnumMedatada(m.group(1),m.group(2),m.group(3)));
			}else {
				throw new IllegalArgumentException("error enumString format:"+data+" expected format:F(1,Female);M(0,Male)");
			}
		}
		return list;
	}
	
}
