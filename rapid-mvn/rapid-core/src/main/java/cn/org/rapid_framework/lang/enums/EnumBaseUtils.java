package cn.org.rapid_framework.lang.enums;

import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;
/**
 * 枚举工具类
 * 
 * @author badqiu
 */
@SuppressWarnings("all")
public class EnumBaseUtils {

    /**
     * 将EnumBase.getCode()作为Key,EnumBase.getDesc()作为value,存放在Map中并返回
     * @param <T>
     * @param values
     * @return
     */
    public static  <T extends EnumBase> LinkedHashMap toMap(Class<? extends EnumBase> enumClass) {
        return toMap(enumClass.getEnumConstants());
    }
    
    /**
     * 将EnumBase.getCode()作为Key,EnumBase.getDesc()作为value,存放在Map中并返回
     * @param <T>
     * @param values
     * @return
     */
    public static  <T extends EnumBase> LinkedHashMap toMap(T[] values) {
        LinkedHashMap map = new LinkedHashMap();
        for(EnumBase item : values) {
            map.put(item.getCode(), item.getDesc());
        }
        return map;
    }
    
    public static <T extends EnumBase<K>,K> K getCode(T enumValue) {
        if(enumValue == null) return null;
        return enumValue.getCode();
    }
   
   public static <T extends EnumBase> String getDesc(T kv) {
       if(kv == null) return null;
       return kv.getDesc();
   }

   public static <T extends Enum> String getName(T kv) {
       if(kv == null) return null;
       return kv.name();
   }

   /**
    * 根据code查找得到Enum
    * @param code
    * @param values
    * @return
    */
   public static <T extends EnumBase> T getByCode(Object code,Class<? extends EnumBase> enumClass) {
       return (T)getByCode(code, enumClass.getEnumConstants());
   }
   
   /**
    * 根据code查找得到Enum
    * @param code
    * @param values
    * @return
    */
   public static <T extends EnumBase> T getByCode(Object code,T[] values) {
       if(code == null) return null;
       if(code instanceof String && StringUtils.isBlank((String)code)) return null;
       
       for (T item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
       }
       return null;
   }

   /**
    * 根据code查找得到Enum
    * @param code
    * @param values
    * @return
    */
   public static <T extends EnumBase> T getRequiredByCode(Object code,Class<? extends EnumBase> enumClass) {
       return (T)getRequiredByCode(code, enumClass.getEnumConstants());
   }
   
   /**
    * 根据code得到Enum,找不到则抛异常
    * @param <T>
    * @param code
    * @param values
    * @return
    * @throws IllegalArgumentException 根据code得到Enum,找不到则抛异常
    */
   public static <T extends EnumBase> T getRequiredByCode(Object code,T[] values) throws IllegalArgumentException {
       if(code == null) return null;
       if(code instanceof String && StringUtils.isBlank((String)code)) return null;
       
       EnumBase v = getByCode(code,values);
       if(v == null) {
           if(values.length > 0) {
               String className = values[0].getClass().getName();
               throw new IllegalArgumentException("not found "+className+" value by code:"+code);
           }else {
               throw new IllegalArgumentException("not found Enum by code:"+code);
           }
       }
       return (T)v;
   }
   
}
