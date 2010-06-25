package cn.org.rapid_framework.common.enums;

import java.util.LinkedHashMap;

@SuppressWarnings("unchecked")
public class EnumIfaceUtils {

    public static  <T extends EnumIface> LinkedHashMap toMap(T[] values) {
        LinkedHashMap map = new LinkedHashMap();
        for(EnumIface item : values) {
            map.put(item.getCode(), item.getDesc());
        }
        return map;
    }
    
   /**
    * 根据code查找得到EnumIface
    * @param code
    * @param values
    * @return
    */
   public static <T extends EnumIface> T getByCode(Object code,T[] values) {
       if(code == null) return null;
       for (T item : values) {
            if (item.getCode().equals(code)) {
                return item;
            }
       }
       return null;
   }
   
   public static <T extends EnumIface> Object getCode(T kv) {
       if(kv == null) return null;
       return kv.getCode();
   }
   
   public static <T extends EnumIface> Object getDesc(T kv) {
       if(kv == null) return null;
       return kv.getDesc();
   }
   
   /**
    * 根据code得到EnumIface,找不到则抛异常
    * @param code
    * @param values
    * @return
    */
   public static <T extends EnumIface> T getRequiredByCode(Object code,T[] values) {
       EnumIface v = getByCode(code,values);
       if(v == null) {
           if(values.length > 0) {
               String className = values[0].getClass().getName();
               throw new IllegalArgumentException("not found "+className+" by key:"+code);
           }else {
               throw new IllegalArgumentException("not found Enum by key:"+code);
           }
       }
       return (T)v;
   }
   
}
