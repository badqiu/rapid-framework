package cn.org.rapid_framework.web.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

public class CookieUtils {
	
	@SuppressWarnings("unchecked")
    public static Map<String,Cookie> toMap(Cookie[] cookies){
    	if(cookies == null || cookies.length == 0)
    		return new HashMap(0);
    	
    	Map map = new HashMap(cookies.length * 2);
    	for(Cookie c : cookies) {
    		map.put(c.getName(), c);
    	}
    	return map;
    }

}
