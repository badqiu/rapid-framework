package cn.org.rapid_framework.generator.ext.tableconfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.XMLHelper;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;

/**
 * 解析ibatis的sql语句,并转换成正常的sql.
 * 
 * 主要功能:
 * 1.将动态构造条件节点全部替换成删除 只留下可执行的SQL
 * 
 */
public class IbatisSqlMapConfigParser {

    public String parse(String str) {
        return parse(str,new HashMap());
    }	

    public String parse(String str,Map<String,String> includeSqls) {
        str = removeComments("<for_remove_comment>"+str+"</for_remove_comment>");
        str = removeSelectKeyXmlForInsertSql(str);
        
        Pattern xmlTagRegex =  Pattern.compile("<(/?[\\w#@]+)(.*?)>");
        StringBuffer sql = new StringBuffer();
        Matcher m = xmlTagRegex.matcher(str);
        
        OpenCloseTag openClose = null;
        while(m.find()) {
            String xmlTag = m.group(1);
            String attributesString = m.group(2);
            Map<String,String> attributes = XMLHelper.parse2Attributes(attributesString);
            
            if("include".equals(xmlTag.trim())) {
                processIncludeByRefid(includeSqls, sql, m, attributes);
                continue;
            }
            
            processForMybatis(sql, xmlTag, attributes);
            
            String replacement = getReplacement(attributes.get("open"), attributes.get("prepend"));
            StringHelper.appendReplacement(m, sql, replacement);
            
            if(openClose != null && openClose.close != null && xmlTag.equals("/"+openClose.xmlTag)) {
                sql.append(openClose.close);
                openClose = null;
            }
            if(attributes.get("close") != null) {
	        	openClose = new OpenCloseTag(); // FIXME 未处理多个open close问题
	        	openClose.xmlTag = xmlTag;
	        	openClose.close = attributes.get("close");
            }
        }
        //FIXME 不能兼容自动删除分号, 因为还需要测试最终的ibatis sql是否会删除;
        return StringHelper.unescapeXml(StringHelper.removeXMLCdataTag(SqlParseHelper.replaceWhere(sql.toString())));
//        return StringHelper.unescapeXml(StringHelper.removeXMLCdataTag(SqlParseHelper.replaceWhere(sql.toString()))).replace(";", "");
    }
    
    private static class OpenCloseTag {
    	public String close;
    	public String xmlTag;
    }

	private static void processForMybatis(StringBuffer sb, String xmlTag,
			Map<String, String> attributes) {
		// mybatis <where>
		if("where".equals(xmlTag.trim())) {
			sb.append("where");
		}
		// mybatis <set>
		if("set".equals(xmlTag.trim())) {
			sb.append("set");
		}
		// mybatis <foreach collection="usernameList" item="item" index="index" open="(" separator="," close=")"> 
		if("foreach".equals(xmlTag.trim())) {
//            	m.appendReplacement(sb, "set"); //FIXME for foreach
		}
		// mybatis <trim prefix="" suffix="" prefixOverrides="" suffixOverrides=""></trim>
		if("trim".equals(xmlTag.trim())) {
			attributes.put("open", attributes.get("prefix"));
			attributes.put("close", attributes.get("suffix")); //FIXME for prefixOverrides,suffixOverrides
		}
	}

	private static String getReplacement(String open, String prepend) {
		String replacement = null;
		if(prepend != null) {
			replacement = " "+prepend+" "+StringHelper.defaultString(open);
		}else {
		    if (StringHelper.isEmpty(open)) {
		    	replacement = "";
		    } else {
		    	replacement = (" " + open);
		    }
		}
		return replacement;
	}
    
    //process <include refid="otherSql"/>
	private void processIncludeByRefid(Map<String, String> includeSqls,
			StringBuffer sb, Matcher m, Map<String, String> attributes) {
		String refid = attributes.get("refid");
		if(refid == null) {
			 m.appendReplacement(sb, "");
		}else {
		    String includeValue = includeSqls.get(refid);
		    if(includeValue == null) throw new IllegalArgumentException("not found include sql by <include refid='"+refid+"'/>");
		    StringHelper.appendReplacement(m, sb, parse(includeValue,includeSqls));
		}
	}
	
	private static String removeSelectKeyXmlForInsertSql(String str) {
    	if(str == null) return null;
    	return str.replaceAll("(?s)<selectKey.*?>.*</selectKey>","");
	}

	public static String removeComments(String str) {
        if(str == null) return null;
        str = str.replaceAll("(?s)<!--.*?-->", "").replaceAll("(?s)/\\*.*?\\*/", "").replace("query not allowed", "");
        return str;
    }

    public static void main(String[] args) throws IOException {
    	IbatisSqlMapConfigParser parser = new IbatisSqlMapConfigParser();
        System.out.println("<abc>123</abc> <diy></diy>".replaceAll("</?\\w*>", ""));
        System.out.println("parsed:"+parser.parse("<isNotEmpty prepend='and' property='gmtCreateStartTime'>BTR.gmt_create &gt;= #gmtCreateStartTime#</isNotEmpty>"));
        System.out.println("parsed:"+parser.parse("<dynamic prepend='WHERE 1=1'>123</dynamic>"));
        System.out.println("parsed:"+parser.parse("select * from user_info"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/test.xml");
		System.out.println("parsed file:"+parser.parse(IOHelper.readFile(file)));
		System.out.println("".equals(removeComments("<!--1\n2\n3-->")));
		System.out.println("".equals(removeComments("/*1\n2\n3*/")));
    }
    
}
