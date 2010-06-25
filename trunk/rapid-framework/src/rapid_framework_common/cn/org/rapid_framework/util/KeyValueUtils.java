package cn.org.rapid_framework.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ReflectionUtils;
/**
 * KeyValue工具类
 * @author zhongxuan
 *
 */
@SuppressWarnings("all")
public class KeyValueUtils {
    
    public static  <T extends KeyValue> LinkedHashMap<String,String> toMap(T[] values) {
        LinkedHashMap map = new LinkedHashMap();
        for(KeyValue item : values) {
            map.put(item.getKey(), item.getValue());
        }
        return map;
    }
    
   /**
    * 根据key查找得到KeyValue
    * @param key
    * @param values
    * @return
    */
   public static <T extends KeyValue> T getByKey(Object key,T[] values) {
       if(key == null) return null;
       for (T item : values) {
            if (item.getKey().equals(key)) {
                return item;
            }
       }
       return null;
   }
   
   /**
    * 根据code得到KeyValue
    * @param key
    * @param values
    * @return
    */
   public static <T extends KeyValue> T getByValue(Object value,T[] values) {
        if(value == null) return null;
        for (T item : values) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
   }

   public static <T extends KeyValue> Object getKey(T kv) {
       if(kv == null) return null;
       return kv.getKey();
   }
   
   public static <T extends KeyValue> Object getValue(T kv) {
       if(kv == null) return null;
       return kv.getValue();
   }
   
   /**
    * 根据code得到KeyValue,找不到则抛异常
    * @param key
    * @param values
    * @return
    */
   public static <T extends KeyValue> T getRequiredByKey(Object key,T[] values) {
       KeyValue v = getByKey(key,values);
       if(v == null) {
           if(values.length > 0) {
               String className = values[0].getClass().getName();
               throw new IllegalArgumentException("not found "+className+" by key:"+key);
           }else {
               throw new IllegalArgumentException("not found KeyValue by key:"+key);
           }
       }
       return (T)v;
   }
   
}
