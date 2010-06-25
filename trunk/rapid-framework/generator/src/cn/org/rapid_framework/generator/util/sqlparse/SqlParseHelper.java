package cn.org.rapid_framework.generator.util.sqlparse;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.StringHelper;


public class SqlParseHelper {
	static Pattern from = Pattern.compile("(from\\s+)([,\\w]+)",Pattern.CASE_INSENSITIVE);
	static Pattern join = Pattern.compile("(join\\s+)(\\w+)",Pattern.CASE_INSENSITIVE);
	static Pattern update = Pattern.compile("(\\s*update\\s+)(\\w+)",Pattern.CASE_INSENSITIVE);
	static Pattern insert = Pattern.compile("(\\s*insert\\s+into\\s+)(\\w+)",Pattern.CASE_INSENSITIVE);
	
	public static Set<String> getTableNamesByQuery(String sql) {
		sql = sql.trim();
		Set<String> result = new LinkedHashSet();
		Matcher m = from.matcher(sql);
		if(m.find()) {
			result.addAll(Arrays.asList(StringHelper.tokenizeToStringArray(m.group(2),",")));
		}
		
		m = join.matcher(sql);
		if(m.find()) {
			result.add(m.group(2));
		}
		
		m = update.matcher(sql);
		if(m.find()) {
			result.add(m.group(2));
		}
		
		m = insert.matcher(sql);
		if(m.find()) {
			result.add(m.group(2));
		}
		return result;
	}
	static Pattern p = Pattern.compile("(:)(\\w+)(\\|?)([\\w.]+)"); 
	public static String getParameterClassName(String sql,String paramName) {
	    Pattern p = Pattern.compile("(:)("+paramName+")(\\|?)([\\w.]+)"); 
	    Matcher m = p.matcher(sql);
	    if(m.find()) {
	        return m.group(4);
	    }
	    return null;
	}
	
	/**
	 * 美化sql
	 * @param sql
	 * @return
	 */
	public static String getPrettySql(String sql)  {
		try {
			if(IOHelper.readLines(new StringReader(sql)).size() > 1) {
				return sql;
			}else {
				return StringHelper.replace(StringHelper.replace(sql,"from","\n\tfrom"),"where","\n\twhere");
			}
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 去除select 子句，未考虑union的情况
	 * @param sql
	 * @return
	 */
    public static String removeSelect(String sql) {
        Assert.hasText(sql);
        int beginPos = sql.toLowerCase().indexOf("from");
        Assert.isTrue(beginPos != -1, " sql : " + sql + " must has a keyword 'from'");
        return sql.substring(beginPos);
    }
	
}
