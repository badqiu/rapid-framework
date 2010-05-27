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
public class KeyValueUtils {
    
    public static  <T extends KeyValue<String,String>> LinkedHashMap<String,String> toMap(T[] values) {
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
   public static <T extends KeyValue> T getByKey(String key,T[] values) {
        for(T item : values) {
            if(item.getKey().equals(key)) {
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
   public static <T extends KeyValue> T getByValue(String value,T[] values) {
        for(T item : values) {
            if(item.getValue().equals(value)) {
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
   public static <T extends KeyValue> T getRequiredByKey(String key,T[] values) {
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
   
   public static LinkedHashMap<Object,String> extractKeyValue(String keyProperty,String valueProperty,Object... arrays) {
       if(arrays == null) return new LinkedHashMap(0);
       return extractKeyValue(keyProperty,valueProperty,Arrays.asList(arrays));
   }
   
   public static LinkedHashMap<Object,String> extractKeyValue(String keyProperty,String valueProperty,List arrays) {
       if(arrays == null) return new LinkedHashMap(0);
       LinkedHashMap<Object,String> map = new LinkedHashMap();
       for(Object obj : arrays) {
           BeanWrapper bw = new BeanWrapperImpl(obj);
           Object key = getPropertyValue(keyProperty, obj);
           Object value = getPropertyValue(valueProperty, obj);
           map.put(key,value == null ? "" : value.toString());
       }
       return map;
   }
   
   //TODO cannot get public field value
   private static Object getPropertyValue(String keyProperty, Object obj){
       try {
           return BeanUtils.getProperty(obj,keyProperty);
       } catch (Exception e) {
           ReflectionUtils.handleReflectionException(e);
           return null;
       }
   }
   
}
