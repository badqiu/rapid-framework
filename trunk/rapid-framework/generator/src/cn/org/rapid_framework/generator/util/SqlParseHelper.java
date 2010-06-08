package cn.org.rapid_framework.generator.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParseHelper {
	static Pattern pattern = Pattern.compile("(from\\s+)(\\w+)",Pattern.CASE_INSENSITIVE);
	
	public static List<String> getTableNamesByQuery(String sql) {
		List<String> result = new ArrayList();
		Matcher m = pattern.matcher(sql);
		if(m.find()) {
			result.add(m.group(2));
		}
		return result;
	}
	
}
