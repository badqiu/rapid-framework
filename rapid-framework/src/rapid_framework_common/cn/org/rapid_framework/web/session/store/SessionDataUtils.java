package cn.org.rapid_framework.web.session.store;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SessionDataUtils {
	static Pattern sessionDataParser = Pattern.compile("\u0000([^:]*):([^\u0000]*)\u0000");
	
	public static String encode(Map<String, String> map) {
		StringBuilder encodeString = new StringBuilder();
		for (String key : map.keySet()) {
			encodeString.append("\u0000");
			encodeString.append(key);
			encodeString.append(":");
			encodeString.append(map.get(key));
			encodeString.append("\u0000");
		}
		return encodeString.toString();
	}

	public static Map decode(String string)   {
		if(string == null) return new HashMap();
		
		Map map = new HashMap();
		Matcher matcher = sessionDataParser.matcher(string);
		while (matcher.find()) {
			map.put(matcher.group(1), matcher.group(2));
		}
		return map;
	}
}
