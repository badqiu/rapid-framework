package cn.org.rapid_framework.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @author badqiu
 */
public class ObjectUtils {

	public static boolean isNullOrEmptyString(Object o) {
		if(o == null)
			return true;
		if(o instanceof String) {
			String str = (String)o;
			if(str.length() == 0)
				return true;
		}
		return false;
	}
	
	/**
	 * 可以用于判断 Map,Collection,String,Array是否为空
	 * @param o
	 * @return
	 */
	@SuppressWarnings("all")
    public static boolean isEmpty(Object o)  {
        if(o == null) return true;

        if(o instanceof String) {
            if(((String)o).length() == 0){
                return true;
            }
        } else if(o instanceof Collection) {
            if(((Collection)o).isEmpty()){
                return true;
            }
        } else if(o.getClass().isArray()) {
            if(Array.getLength(o) == 0){
                return true;
            }
        } else if(o instanceof Map) {
            if(((Map)o).isEmpty()){
                return true;
            }
        }else {
            return false;
        }

        return false;
    }
	
	/**
	 * 得到Array,Collection,Map的大小
	 * @param o
	 * @return
	 */
    public static int getSize(Object o)  {
        if(o == null) return 0;

        if(o instanceof Collection) {
            return ((Collection)o).size();
        } else if(o.getClass().isArray()) {
            return Array.getLength(o);
        } else if(o instanceof Map) {
            return ((Map)o).size();
        }else {
            return 1;
        }
    }
    
	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * @param c
	 * @return
	 */
	public static boolean isNotEmpty(Object c) throws IllegalArgumentException{
		return !isEmpty(c);
	}
	
}
