package cn.org.rapid_framework.generator.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.rapid_framework.generator.provider.db.model.Column.EnumMetadada;

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
	 * 将string转换为List<ColumnEnum> 格式为: "enumAlias(enumKey,enumDesc)"
	 */
	static Pattern three = Pattern.compile("(.*)\\((.*),(.*)\\)");
	static Pattern two = Pattern.compile("(.*)\\((.*)\\)");
	public static List<EnumMetadada> string2EnumMetadata(String data) {
		if(data == null || data.trim().isEmpty()) return new ArrayList();
		//enumAlias(enumKey,enumDesc),enumAlias(enumDesc)
		
		List<EnumMetadada> list = new ArrayList();
		String[] data_arr = data.split(";");
		for (int i = 0; i < data_arr.length; i++) {
			Matcher three_m = three.matcher(data_arr[i]);
			if(three_m.find()) {
				list.add(new EnumMetadada(three_m.group(1),three_m.group(2),three_m.group(3)));
				continue;
			}
			Matcher two_m = two.matcher(data_arr[i]);
			if(two_m.find()) {
				list.add(new EnumMetadada(two_m.group(1),two_m.group(1),two_m.group(2)));
				continue;
			}			
			throw new IllegalArgumentException("error enumString format:"+data+" expected format:F(1,Female);M(0,Male) or F(Female);M(Male)");
		}
		return list;
	}
	
}
