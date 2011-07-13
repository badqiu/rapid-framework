package com.alibaba.tctools.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 转换Map的key值映射
 * TODO Comment of ModelConvertUtil
 * @author lai.zhoul
 * 2011-7-8
 */
public class ModelConvertUtil {
	
	/**
	 * 处理TsvModel的Map Key映射
	 * @param list
	 * @param configMap
	 * @return
	 */
	public static List<Map<String,String>> convert(List<Map<String,String>> list,Map<String,String> configMap){
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		for(Map<String,String> item:list){
			result.add(convert(item, configMap));	
		}
		return result;
	}
	
	private static Map<String,String> convert(Map<String,String> oldMap,Map<String,String> configMap ){
		Map<String,String> result=new HashMap<String, String>(); 
		for(Entry<String, String> item :oldMap.entrySet()){
			if(configMap.containsKey(item.getKey())){ //若已经配置key的映射则取映射值作为新key
				result.put(configMap.get(item.getKey()), item.getValue());
			}else{//若未配置key的映射，则取原来的key值
				result.put(item.getKey(), item.getValue());
			}
		}
		return result;
	}
}
