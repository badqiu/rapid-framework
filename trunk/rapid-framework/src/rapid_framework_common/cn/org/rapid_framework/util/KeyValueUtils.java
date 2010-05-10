package cn.org.rapid_framework.util;

import java.util.LinkedHashMap;

import org.apache.commons.collections.KeyValue;
/**
 * KeyValue工具类
 * @author zhongxuan
 *
 */
public class KeyValueUtils {
    
    public static LinkedHashMap<String,String> toMap(KeyValue[] values) {
        LinkedHashMap map = new LinkedHashMap();
        for(KeyValue item : values) {
            map.put(item.getKey(), item.getValue());
        }
        return map;
    }
    
   /**
    * 根据code得到KeyValue
    * @param code
    * @param values
    * @return
    */
   public static KeyValue getByKey(String code,KeyValue[] values) {
        for(KeyValue item : values) {
            if(item.getKey().equals(code)) {
                return item;
            }
        }
        return null;
    }
   
   /**
    * 根据code得到KeyValue,找不到则抛异常
    * @param code
    * @param values
    * @return
    */
   public static KeyValue getRequiredByKey(String code,KeyValue[] values) {
       KeyValue v = getByKey(code,values);
       if(v == null) {
           if(values.length > 0) {
               throw new IllegalArgumentException("not found enum by code:"+code+" on "+values[0].getClass().getName() );
           }else {
               throw new IllegalArgumentException("not found enum by code:"+code);
           }
       }
       return v;
   }
   
}
