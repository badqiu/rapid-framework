package cn.org.rapid_framework.generator.ext.tableconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.rapid_framework.generator.provider.db.sql.model.SqlSegment;
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
    public Map<String,SqlSegment> usedIncludedSqls = new HashMap(); //增加一条sql语句引用的 includesSql的解析
    private String sourceSql;
    private Map<String,String> includeSqls;
    public String resultSql;
    
    public String parse(String str) {
        return parse(str,new HashMap());
    }	

    public String parse(String str,Map<String,String> includeSqls) {
    	this.sourceSql = str;
    	this.includeSqls = includeSqls;
        str = Helper.removeComments("<for_remove_comment>"+str+"</for_remove_comment>");
        str = Helper.removeSelectKeyXmlForInsertSql(str);
        
        Pattern xmlTagRegex =  Pattern.compile("<(/?[\\w#@]+)(.*?)>");
        StringBuffer sql = new StringBuffer();
        Matcher m = xmlTagRegex.matcher(str);
        
        OpenCloseTag openClose = null;
        Map previousTagAttributes = null;
        while(m.find()) {
            String xmlTag = m.group(1);
            String attributesString = m.group(2);
            Map<String,String> attributes = XMLHelper.parse2Attributes(attributesString);
            
            if("include".equals(xmlTag.trim())) {
                processIncludeByRefid(includeSqls, sql, m, attributes);
                continue;
            }
            
            MybatisHelper.processForMybatis(sql, xmlTag, attributes);
            
            String replacement = Helper.getReplacement(attributes.get("open"), attributes.get("prepend"));
            StringHelper.appendReplacement(m, sql, replacement);
            
            openClose = processOpenClose(sql, openClose, xmlTag, attributes);
            
            MybatisHelper.processMybatisForeachCloseTag(sql, previousTagAttributes, xmlTag);
            
            previousTagAttributes = attributes;
        }
        //FIXME 不能兼容自动删除分号, 因为还需要测试最终的ibatis sql是否会删除;
        resultSql = StringHelper.unescapeXml(StringHelper.removeXMLCdataTag(SqlParseHelper.replaceWhere(sql.toString())));
        return resultSql;
//        return StringHelper.unescapeXml(StringHelper.removeXMLCdataTag(SqlParseHelper.replaceWhere(sql.toString()))).replace(";", "");
    }

	private OpenCloseTag processOpenClose(StringBuffer sql,
			OpenCloseTag openClose, String xmlTag,
			Map<String, String> attributes) {
		if(openClose != null && openClose.close != null && xmlTag.equals("/"+openClose.xmlTag)) {
		    sql.append(openClose.close);
		    openClose = null;
		}
		if(attributes.get("close") != null) {
			openClose = new OpenCloseTag(); // FIXME 未处理多个open close问题
			openClose.xmlTag = xmlTag;
			openClose.close = attributes.get("close");
		}
		return openClose;
	}

    public List<SqlSegment> getSqlSegments() {
    	return new ArrayList(usedIncludedSqls.values());
    }
    
    private static class OpenCloseTag {
    	public String close;
    	public String xmlTag;
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
            String parsedIncludeValue = parse(includeValue,includeSqls);
            usedIncludedSqls.put(refid, new SqlSegment(refid,includeValue,parsedIncludeValue));
			StringHelper.appendReplacement(m, sb, parsedIncludeValue);
        }
    }
    
    static class MybatisHelper {
    	private static void processMybatisForeachCloseTag(StringBuffer sql, Map preTagAttributes,
    			String xmlTag) {
    		// mybatis username in <foreach collection="usernameList" item="item" index="index" open="(" separator="," close=")">#{item}<foreach>
    		if ("/foreach".equals(xmlTag.trim())) {
    			// 将 username in (#{item}) 替换为 username in (#collection[]#)
    			String item = (String)preTagAttributes.get("item");
    			String collection = (String)preTagAttributes.get("collection");
    			String tempSql = StringHelper.replace(sql.toString(), "#{"+item+"}", "#"+collection+"[]#");
    			tempSql = StringHelper.replace(tempSql.toString(), "${"+item+"}", "$"+collection+"[]$");
    			sql.setLength(0);
    			sql.append(tempSql);
    		}
    	}
    	
        private static void processForMybatis(StringBuffer sb, String xmlTag,
                                              Map<String, String> attributes) {
            // mybatis <where>
            if ("where".equals(xmlTag.trim())) {
                attributes.put("open", "where");
            }
            // mybatis <set>
            if ("set".equals(xmlTag.trim())) {
            	attributes.put("open", "set");
            }
            // mybatis <foreach collection="usernameList" item="item" index="index" open="(" separator="," close=")">
            if ("foreach".equals(xmlTag.trim())) {
                // m.appendReplacement(sb, "set"); //FIXME for foreach
            }

            // mybatis <trim prefix="" suffix="" prefixOverrides=""
            // suffixOverrides=""></trim>
            if ("trim".equals(xmlTag.trim())) {
                attributes.put("open", attributes.get("prefix"));
                attributes.put("close", attributes.get("suffix")); // FIXME for
                                                                   // prefixOverrides,suffixOverrides
                													// <trim prefix="SET" suffixOverrides=",">
                													// <trim prefix="WHERE" prefixOverrides="AND |OR ">
            }
        }    	
    }
    static class Helper {

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
    	
    	private static String removeSelectKeyXmlForInsertSql(String str) {
        	if(str == null) return null;
        	return str.replaceAll("(?s)<selectKey.*?>.*</selectKey>","");
    	}
    
    	public static String removeComments(String str) {
            if(str == null) return null;
            str = str.replaceAll("(?s)<!--.*?-->", "").replaceAll("(?s)/\\*.*?\\*/", "").replace("query not allowed", "");
            return str;
        }
    }

}
