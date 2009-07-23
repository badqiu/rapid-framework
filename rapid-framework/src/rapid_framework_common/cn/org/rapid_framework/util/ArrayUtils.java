package cn.org.rapid_framework.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class ArrayUtils {
	
	/**
	 * 将一个array转为根据keys转为map
	 * @param array
	 * @param keys
	 * @return
	 */
	public static Map toMap(String[] array,String...keys) {
		Map m = new LinkedHashMap();
		for(int i = 0; i < keys.length; i++) {
			if(array.length == i ) {
				break;
			}
			m.put(keys[i], array[i]);
		}
		return m;
	}
	
}
