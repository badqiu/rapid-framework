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
	
	public static String encode(Map<String, String> map) throws UnsupportedEncodingException {
		StringBuilder flash = new StringBuilder();
		for (String key : map.keySet()) {
			flash.append("\u0000");
			flash.append(key);
			flash.append(":");
			flash.append(map.get(key));
			flash.append("\u0000");
		}
		return flash.toString();
	}

	public static Map decode(String string) throws UnsupportedEncodingException {
		Map map = new HashMap();
		Matcher matcher = sessionDataParser.matcher(string);
		while (matcher.find()) {
			map.put(matcher.group(1), matcher.group(2));
		}
		return map;
	}
}
