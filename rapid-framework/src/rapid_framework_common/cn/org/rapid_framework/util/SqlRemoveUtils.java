package cn.org.rapid_framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

public class SqlRemoveUtils {

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

    /**
     * 去除orderby 子句
     * @param sql
     * @return
     */
    public static String removeOrders(String sql) {
        Assert.hasText(sql);
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }
    
    public static String removeFetchKeyword(String sql) {
    	return sql.replaceAll("(?i)fetch", "");
    }

	public static String removeXsqlBuilderOrders(String string) {
        Assert.hasText(string);
        Pattern p = Pattern.compile("/~.*order\\s*by[\\w|\\W|\\s|\\S]*~/", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return removeOrders(sb.toString());
	}

}
