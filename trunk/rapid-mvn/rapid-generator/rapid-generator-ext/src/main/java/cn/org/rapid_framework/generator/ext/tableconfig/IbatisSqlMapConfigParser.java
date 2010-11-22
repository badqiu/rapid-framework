package cn.org.rapid_framework.generator.ext.tableconfig;

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

    // 1. 处理 query not allowed
    // 2. order by可能多个问题，应该移除: where子句，order by子句,having子句, group by保留
    // 同时支持 <include refid="otherSql"/> <sql id=""></sql>
    public static String parse(String str,Map<String,String> includeSqls) {
        str = removeComments("<for_remove_comment>"+str+"</for_remove_comment>");
        str = removeSelectKeyXmlForInsertSql(str);
        Pattern xmlTagRegex =  Pattern.compile("<(/?[\\w#@]+)(.*?)>");
        StringBuffer sb = new StringBuffer();
        Matcher m = xmlTagRegex.matcher(str);
        
        OpenCloseTag openClose = null;
        while(m.find()) {
            String xmlTag = m.group(1);
            String attributesString = m.group(2);
            Map<String,String> attributes = XMLHelper.parse2Attributes(attributesString);
            
            if("include".equals(xmlTag.trim())) {
                processIncludeByRefid(includeSqls, sb, m, attributes);
                continue;
            }
            
            processForMybatis(sb, xmlTag, attributes);
            
            String replacement = getReplacement(attributes.get("open"), attributes.get("prepend"));
            StringHelper.appendReplacement(m, sb, replacement);
            
            if(openClose != null && openClose.close != null && xmlTag.equals("/"+openClose.xmlTag)) {
                sb.append(openClose.close);
                openClose = null;
            }
            if(attributes.get("close") != null) {
	        	openClose = new OpenCloseTag(); // FIXME 未处理多个open close问题
	        	openClose.xmlTag = xmlTag;
	        	openClose.close = attributes.get("close");
            }
        }
        return StringHelper.removeXMLCdataTag(SqlParseHelper.replaceWhere(sb.toString()));
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
	private static void processIncludeByRefid(Map<String, String> includeSqls,
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
        System.out.println("<abc>123</abc> <diy></diy>".replaceAll("</?\\w*>", ""));
        System.out.println("parsed:"+parse("<isNotEmpty prepend='and' property='gmtCreateStartTime'>BTR.gmt_create &gt;= #gmtCreateStartTime#</isNotEmpty>"));
        System.out.println("parsed:"+parse("<dynamic prepend='WHERE 1=1'>123</dynamic>"));
        System.out.println("parsed:"+parse("select * from user_info"));
        File file = FileHelper.getFileByClassLoader("cn/org/rapid_framework/generator/ext/tableconfig/test.xml");
		System.out.println("parsed file:"+parse(IOHelper.readFile(file)));
		System.out.println("".equals(removeComments("<!--1\n2\n3-->")));
		System.out.println("".equals(removeComments("/*1\n2\n3*/")));
    }
    
}
