package cn.org.rapid_framework.generator.ext.ibatis;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.XMLHelper;
/**
 * 解析sql map文件，生成Sql对象
 */
public class IbatisSqlMapConfigParser {
    //1. 处理  query not allowed
    //2. order by可能多个问题，应该移除: where子句，order by子句,having子句, group by保留
    public static String parse(String str) {
        str = removeComments("<sql>"+str+"</sql>");
        Pattern p =  Pattern.compile("</?\\w+(.*?)>");
        StringBuffer sb = new StringBuffer();
        Matcher m = p.matcher(str);
        
        String open = null;
        String close = null;
        while(m.find()) {
            Map<String,String> attributes = XMLHelper.parse2Attributes(m.group(1));
            String prepend = attributes.get("prepend");
            
            open = attributes.get("open");
            if(prepend != null) {
                m.appendReplacement(sb, " "+prepend+" "+StringHelper.defaultString(open));
            }else {
                m.appendReplacement(sb, " "+StringHelper.defaultString(open));
            }
            if(close != null) {
                sb.append(close);
            }
            
            open = null;
            close = attributes.get("close");
        }
        return sb.toString().replaceAll("(?i)where\\s+and", "WHERE");
    }
    
    private static String removeComments(String str) {
        if(str == null) return null;
        return str.replaceAll("<!--.*?-->", "").replaceAll("/\\*.*?\\*/", "").replace("query not allowed", "");
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("<abc>123</abc> <diy></diy>".replaceAll("</?\\w*>", ""));
        System.out.println("parsed:"+parse("<isNotEmpty prepend='and' property='gmtCreateStartTime'>BTR.gmt_create &gt;= #gmtCreateStartTime#</isNotEmpty>"));
        System.out.println("parsed:"+parse("<dynamic prepend='WHERE 1=1'>123</dynamic>"));
        System.out.println("parsed:"+parse("select * from user_info"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/ibatis/test.xml");
		System.out.println("parsed file:"+parse(IOHelper.readFile(file)));
    }
}
