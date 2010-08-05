package cn.org.rapid_framework.generator.ext.ibatis;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.XMLHelper;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;
/**
 * 解析sql map文件，生成Sql对象
 */
public class IbatisSqlMapConfigParser extends SqlFactory {
	protected String beforeParseSql(String sourceSql) {
		String parsedSql = parse(sourceSql);
		String namedSql = SqlParseHelper.convert2NamedParametersSql(parsedSql,"#","#");
		return namedSql;
	}
	
	@Override
	protected Sql afterProcessedSql(Sql sql) {
		return super.afterProcessedSql(sql);
	}
	

	
    public static String parse(String str) {
        return parse(str,new HashMap());
    }	

    //1. 处理  query not allowed
    //2. order by可能多个问题，应该移除: where子句，order by子句,having子句, group by保留
    // 同时支持 <include refid="otherSql"/> <sql id=""></sql>
    public static String parse(String str,Map<String,String> includeSqls) {
        str = removeComments("<for_remove_comment>"+str+"</for_remove_comment>");
        Pattern xmlTagRegex =  Pattern.compile("</?(\\w+)(.*?)>");
        StringBuffer sb = new StringBuffer();
        Matcher m = xmlTagRegex.matcher(str);
        
        String open = null;
        String close = null;
        while(m.find()) {
            String xmlTag = m.group(1);
            String attributesString = m.group(2);
            
            Map<String,String> attributes = XMLHelper.parse2Attributes(attributesString);
            //process <include refid="otherSql"/>
            if(xmlTag.startsWith("include")) {
                String refid = attributes.get("refid");
                String includeValue = includeSqls.get(refid);
                if(includeValue == null) throw new IllegalArgumentException("not found include sql by <include refid='"+refid+"'/>");
                m.appendReplacement(sb, includeValue);
                continue;
            }
            
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
    
    public static String removeComments(String str) {
        if(str == null) return null;
        return str.replaceAll("(?s)<!--.*?-->", "").replaceAll("(?s)/\\*.*?\\*/", "").replace("query not allowed", "");
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("<abc>123</abc> <diy></diy>".replaceAll("</?\\w*>", ""));
        System.out.println("parsed:"+parse("<isNotEmpty prepend='and' property='gmtCreateStartTime'>BTR.gmt_create &gt;= #gmtCreateStartTime#</isNotEmpty>"));
        System.out.println("parsed:"+parse("<dynamic prepend='WHERE 1=1'>123</dynamic>"));
        System.out.println("parsed:"+parse("select * from user_info"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/ibatis/test.xml");
		System.out.println("parsed file:"+parse(IOHelper.readFile(file)));
		System.out.println("".equals(removeComments("<!--1\n2\n3-->")));
		System.out.println("".equals(removeComments("/*1\n2\n3*/")));
    }
    
}
