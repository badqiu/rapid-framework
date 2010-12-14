package cn.org.rapid_framework.generator.ext.tableconfig;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.provider.db.sql.model.SqlParameter;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.XMLHelper;
import cn.org.rapid_framework.generator.util.sqlparse.NamedParameterUtils;
import cn.org.rapid_framework.generator.util.sqlparse.ParsedSql;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;

/**
 * 解析ibatis的sql语句,并转换成正常的sql.
 * 
 * 主要功能:
 * 1.将动态构造条件节点全部替换成删除 只留下可执行的SQL
 * 
 */
public class IbatisSqlMapConfigParser {
    public Map<String,UsedIncludeSql> usedIncludedSqls = new HashMap(); //增加一条sql语句引用的 includesSql的解析
    private String sourceSql;
    private Map<String,String> includeSqls;
    private String resultSql;
    
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
        while(m.find()) {
            String xmlTag = m.group(1);
            String attributesString = m.group(2);
            Map<String,String> attributes = XMLHelper.parse2Attributes(attributesString);
            
            if("include".equals(xmlTag.trim())) {
                processIncludeByRefid(includeSqls, sql, m, attributes);
                continue;
            }
            
            Helper.processForMybatis(sql, xmlTag, attributes);
            
            String replacement = Helper.getReplacement(attributes.get("open"), attributes.get("prepend"));
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
        resultSql = StringHelper.unescapeXml(StringHelper.removeXMLCdataTag(SqlParseHelper.replaceWhere(sql.toString())));
        return resultSql;
//        return StringHelper.unescapeXml(StringHelper.removeXMLCdataTag(SqlParseHelper.replaceWhere(sql.toString()))).replace(";", "");
    }
    
    public void test() {
    	for(UsedIncludeSql sql : usedIncludedSqls.values()) {
    		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql.parsedIncludeSql);
    	}
    }
    private static class OpenCloseTag {
    	public String close;
    	public String xmlTag;
    }
    
    public class UsedIncludeSql {
    	public String refid;
    	public String rawIncludeSql;
    	public String parsedIncludeSql;
    	public Set<SqlParameter> params;
    	
    	public UsedIncludeSql(String refid, String rawIncludeSql,
				String parsedIncludeSql) {
			super();
			this.refid = refid;
			this.rawIncludeSql = rawIncludeSql;
			this.parsedIncludeSql = parsedIncludeSql;
		}

		public Set<SqlParameter> getParams() {
			Sql sql = new SqlFactory().parseSql(resultSql); // FIXME 缺少自定义参数类型
    		Set<SqlParameter> result = new LinkedHashSet();
			for(String paramName : getParamNames()) {
    			SqlParameter p = sql.getParam(paramName);
    			if(p == null) throw new IllegalArgumentException("not found param on sql:"+parsedIncludeSql+" with name:"+paramName); //是否不该扔异常
    			result.add(p);
			}
			return result;
    	}
		public List<String> getParamNames() {
			ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(parsedIncludeSql);
			return parsedSql.getParameterNames();
		}
		public String getRefidClassName() {
			return StringHelper.toJavaClassName(refid.replace(".", "_").replace("-", "_"));
		}
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
            usedIncludedSqls.put(refid, new UsedIncludeSql(refid,includeValue,parsedIncludeValue));
			StringHelper.appendReplacement(m, sb, parsedIncludeValue);
        }
    }
    
    static class Helper {
        
        private static void processForMybatis(StringBuffer sb, String xmlTag,
                                              Map<String, String> attributes) {
            // mybatis <where>
            if ("where".equals(xmlTag.trim())) {
                sb.append("where");
            }
            // mybatis <set>
            if ("set".equals(xmlTag.trim())) {
                sb.append("set");
            }
            // mybatis <foreach collection="usernameList" item="item"
            // index="index" open="(" separator="," close=")">
            if ("foreach".equals(xmlTag.trim())) {
                // m.appendReplacement(sb, "set"); //FIXME for foreach
            }
            // mybatis <trim prefix="" suffix="" prefixOverrides=""
            // suffixOverrides=""></trim>
            if ("trim".equals(xmlTag.trim())) {
                attributes.put("open", attributes.get("prefix"));
                attributes.put("close", attributes.get("suffix")); // FIXME for
                                                                   // prefixOverrides,suffixOverrides
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
