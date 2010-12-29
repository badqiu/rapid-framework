package cn.org.rapid_framework.generator.ext.tableconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;
import cn.org.rapid_framework.generator.provider.db.sql.model.Sql;
import cn.org.rapid_framework.generator.provider.db.sql.model.SqlParameter;
import cn.org.rapid_framework.generator.provider.db.sql.model.SqlSegment;
import cn.org.rapid_framework.generator.util.BeanHelper;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.test.GeneratorTestHelper;

public class IbatisSqlMapConfigParserTest extends GeneratorTestCase {
	HashMap<String,String> hashMap = new HashMap();
	IbatisSqlMapConfigParser parser = new IbatisSqlMapConfigParser();
	public void setUp() throws Exception {
		super.setUp();
		hashMap.put("question", "username = ? and password = ?");
		hashMap.put("placeholder", "sex = #equals_sex# and password = #pwd#");
	}
	public static void assertMatch(String str,String regex) {
		assertTrue("Regex:"+regex+" str:"+str,StringHelper.indexOfByRegex(str, regex) >= 0);
	}
	public void test_freemarker_xml() {
	    assertTrue("".equals(IbatisSqlMapConfigParser.Helper.removeComments("<!--1\n2\n3-->")));
	    assertTrue("".equals(IbatisSqlMapConfigParser.Helper.removeComments("/*1\n2\n3*/")));
	        
		stringEquals("#abc123[]# ",parser.parse("<#if databaseType?is_empty>#abc123[]# </#if>"));
		stringEquals("#abc123[]# ",parser.parse("<#if databaseType?is_empty>#abc123[]# </#if>"));
		stringEquals("#abc123[]# ",parser.parse("<@if databaseType?is_empty>#abc123[]# </@if>"));
	}
	
	public void test_remove_semicolon() {
//	    assertEquals("select * from user_info",parser.parse("select * from user_info;")); //TODO 自动删除;号
//	    assertEquals("select * from user_info select * from user_info      ",parser.parse("select * from user_info; select * from user_info;      "));
//	    assertEquals("select * from user_info &",parser.parse("select * from user_info &amp;"));
	}
	
	public void test() {
	    assertEquals(" WHERE order_id = ?",parser.parse(" WHERE order_id = ?"));
	    assertEquals(" WHERE ander_id = ?",parser.parse(" WHERE ander_id = ?"));
	    assertEquals(" WHERE user = ?",parser.parse(" WHERE and user = ?"));
	    assertEquals(" WHERE user = ?",parser.parse(" WHERE or user = ?"));
	    assertEquals(" WHERE user = ?",parser.parse(" where or user = ?"));
	    assertEquals(" WHERE user = ?",parser.parse(" where OR user = ?"));
	}
	
	@SuppressWarnings("static-access")
	public void test_iterate_open_and_close() {
		stringEquals(
            " (#abc123[]# )",
            parser
                .parse("<iterate open='(' close=')' conjunction='OR' property='orIncludeAges'>#abc123[]# </iterate>"));
        stringEquals(
            "  (#abc123[]# ) ",
            parser
                .parse("<iterate open='(' close=')' conjunction=',' property='orIncludeAges'>#abc123[]# </iterate>"));
		stringEquals(
            "  (#abc123[]# ",
            parser
                .parse("<iterate open='(' close=')' conjunction='OR' property='orIncludeAges'>#abc123[]#"));
	}

	public void test_iterate_open() {
		stringEquals(
            "  (#abc123[]#   ",
            parser
                .parse("<iterate open='(' conjunction='OR' property='orIncludeAges'>#abc123[]# </iterate>"));
        stringEquals(
            "  #abc123[]#  ) ",
            parser
                .parse("<iterate close=')' conjunction=',' property='orIncludeAges'>#abc123[]# </iterate>"));
        stringEquals("  #abc123[]# ", parser
            .parse("<iterate property='orIncludeAges'>#abc123[]#"));
	}
	
	public void test_include_by_refid() {
		stringEquals("  username = ? and password = ?  ", parser.parse(
            "<include refid='question'/>", hashMap));
		stringEquals("  sex = #equals_sex# and password = #pwd#  ", parser
            .parse("<include refid='placeholder'/>", hashMap));
        stringEquals(
            "   begin__sex = #equals_sex# and password = #pwd# end   ",
            parser.parse("begin__<include refid='placeholder'/> end", hashMap));
        stringEquals("  begin__sex = #equals_sex# and password = #pwd# end ",
            parser.parse("begin__<include refid='placeholder'><include/> end",
                hashMap));
		stringEquals(
            " begin__sex = #equals_sex# and password = #pwd# inner text  end ",
            parser
                .parse(
                    "begin__<include refid='placeholder'> inner text <include/> end",
                    hashMap));
		
		try {
		stringEquals(" username = ? and password = ? ", parser.parse(
                "<include refid='question'/>", new HashMap()));
		fail();
		}catch(IllegalArgumentException e) {
			
		}
	}
	
	public void test_prepend() {
		stringEquals(
            "  AND age = ?  ",
            parser
                .parse("<isNotEmpty prepend='AND' property='age'>age = ?</isNotEmpty>"));
        stringEquals(
            "  sex! age = ?  ",
            parser
                .parse("<isNotEmpty prepend='sex!' property='age'>age = ?</isNotEmpty>"));
        stringEquals("  age = ?  ", parser
            .parse("<isNotEmpty property='age'>age = ?</isNotEmpty>"));
	}
	
	public void test_xml_escaped() {
		stringEquals(
            "  3&2<1> age = ?  ",
            parser
                .parse("<isNotEmpty prepend='3&amp;2&lt;1&gt;' property='age'>age = ?</isNotEmpty>"));
        stringEquals(
            "  3&2<1> age < ?  ",
            parser
                .parse("<isNotEmpty prepend='3&amp;2&lt;1&gt;' property='age'>age &lt; ?</isNotEmpty>"));
        stringEquals(
            "  3&2<1> age >= ?  ",
            parser
                .parse("<isNotEmpty prepend='3&amp;2&lt;1&gt;' property='age'>age &gt;= ?</isNotEmpty>"));
	}

	public void test_remove_where() {
		stringEquals(
            "  WHERE 1=1  ",
            parser
                .parse("<isNotEmpty prepend='where' property='age'>and 1=1</isNotEmpty>"));
        stringEquals(
            "  WHERE 1=1  ",
            parser
                .parse("<isNotEmpty prepend='wHere' property='age'>And 1=1</isNotEmpty>"));
        stringEquals(
            "  WHERE 1=1  ",
            parser
                .parse("<isNotEmpty prepend='whERe' property='age'>AnD 1=1</isNotEmpty>"));
        stringEquals(
            " select * from user  whERe 1=1  ",
            parser
                .parse("select * from user <isNotEmpty prepend='whERe' property='age'>1=1</isNotEmpty>"));
        stringEquals(" select * from user WHERE 1=1 ", parser
            .parse("select * from user where and 1=1"));
        stringEquals(" select * from user\nWHERE 1=1 ", parser
            .parse("select * from user\nwhere \n\nand 1=1"));
	}

	public void test_remove_comments() {
		stringEquals(
            " and 1=1  ",
            parser
                .parse("<!--<isNotEmpty prepend='where' property='age'>\n-->and 1=1</isNotEmpty>"));
        stringEquals(
            " And 1=1  ",
            parser
                .parse("/*<isNotEmpty prepend='wHere' property='age'>\n*/And 1=1</isNotEmpty>"));
        stringEquals(
            "  whERe   ",
            parser
                .parse("<isNotEmpty prepend='whERe' property='age'>query not allowed</isNotEmpty>"));
	}
	
	private void stringEquals(String str1, String str2) {
        assertTrue("expected:" + str1 + " actual:" + str2, str1.replaceAll(
            "(?m)\\s+", "").equals(str2.replaceAll("(?m)\\s+", "")));
    }

    public void test_include_by_refid_will_empty() {
		assertEquals("", parser.parse("<include />", hashMap));
		
		assertEquals("多了前后空格是错误的", "sex = #equals_sex# and password = #pwd#",
            parser.parse("<include refid='placeholder'/>", hashMap));
	}
    
    public void testRemoveSelectKey() {
    	String v = parser.parse("123<selectKey resultClass='java.lang.Long' type='post' keyProperty='user_id' >\nselect last_insert_id() from any\n</selectKey>");
    	assertEquals("123",v);
    }
    
    public void test_includeSqlParams() {
    	hashMap.put("user-Info.where", "username = #username# and password = #password# and age = #age# ");
    	parser.parse("select * from user_info where <include refid='user-Info.where'/>",hashMap);
    	for(SqlSegment segment : parser.usedIncludedSqls.values()) {
    		assertEquals(segment.getParamNames().get(0),"username");
    		assertEquals(segment.getParamNames().get(1),"password");
    		assertEquals(segment.getParamNames().get(2),"age");
    		assertEquals(segment.getClassName(),"UserInfoWhere");
    		
    		Sql sql = new SqlFactory().parseSql(parser.resultSql);
    		assertEquals(get(segment.getParams(sql),0).getParamName(),"username");
    		assertEquals(get(segment.getParams(sql),1).getParamName(),"password");
    		assertEquals(get(segment.getParams(sql),2).getParamName(),"age");
    		
    		assertEquals(get(segment.getParams(sql),0).getParameterClass(),"String");
    		assertEquals(get(segment.getParams(sql),1).getParameterClass(),"String");
    		assertEquals(get(segment.getParams(sql),2).getParameterClass(),"Integer");
    	}
    }
    
    public void test_get_includeSqlParams() throws Exception {
    	hashMap.put("user-Info.where", "username = #username# and password = #password# and age = #age# ");
    	parser.parse("select * from user_info where <include refid='user-Info.where'/>",hashMap);
    	
    	GeneratorFacade gf = new GeneratorFacade();
    	gf.getGenerator().setTemplateRootDir("classpath:for_test_sql_segment");
    	for(SqlSegment segment : parser.usedIncludedSqls.values()) {
    		segment.setParams(segment.getParams(new SqlFactory().parseSql(parser.resultSql)));
    		Map map = new HashMap();
    		map.put("sqlSegment", segment);
    		map.putAll(BeanHelper.describe(segment));
    		System.out.println(map);
    		String str = GeneratorTestHelper.generateByMap(gf, map);
    		System.out.println(str);
    		assertFalse(StringHelper.isBlank(str));
    		assertMatch(str,"public class UserInfoWhere");
    		assertMatch(str,"private String username");
    		assertMatch(str,"private String password");
    		assertMatch(str,"private Integer age");
    	}
    }

    public void test_includeSqlParams_with_question_nation() {
    	hashMap.put("User.Info-where", "username = #username# and password = #password# and age = ? ");
    	parser.parse("select * from user_info where <include refid='User.Info-where'/>",hashMap);
    	for(SqlSegment segment : parser.usedIncludedSqls.values()) {
    		assertEquals(segment.getParamNames().get(0),"username");
    		assertEquals(segment.getParamNames().get(1),"password");
//    		assertEquals(segment.getParamNames().get(2),"age"); // TODO 现在不支持 age = ?
    		assertEquals(segment.getClassName(),"UserInfoWhere");
    		
    		Sql sql = new SqlFactory().parseSql(parser.resultSql);
    		assertEquals(get(segment.getParams(sql),0).getParamName(),"username");
    		assertEquals(get(segment.getParams(sql),1).getParamName(),"password");
//    		assertEquals(get(segment.getParams(sql),2).getParamName(),"age");
    		
    		assertEquals(get(segment.getParams(sql),0).getParameterClass(),"String");
    		assertEquals(get(segment.getParams(sql),1).getParameterClass(),"String");
//    		assertEquals(get(segment.getParams(sql),2).getParameterClass(),"Integer");
    	}
    }
    
    public void testMybatisForeachTag() {
    	stringEquals("username in (#list[]#)",parser.parse("username in <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach>"));
    	stringEquals("username in ($list[]$)",parser.parse("username in <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>${item}</foreach>"));
    }
    
    public void testMybatisTrimTag() {
    	stringEquals("select * from user where 1=2 and username = ? ",parser.parse("select * from user <where> 1=2 and username = ? </where>"));
    	stringEquals("select * from user where 1=2 and username = ? ",parser.parse("select * from user <where> 1=2 and username = ? </where>"));
    	stringEquals("select * from user set 1=2",parser.parse("select * from user <set> 1=2 </set>"));
    	stringEquals("select * from user trimPrefix 1=2",parser.parse("select * from user <trim prefix='trimPrefix'> 1=2 </trim>"));
    	stringEquals("select * from user trimPrefix 1=2 trimSuffix",parser.parse("select * from user <trim prefix='trimPrefix' suffix='trimSuffix'> 1=2 </trim>"));
    }
    
	private SqlParameter get(Set<SqlParameter> params, int i) {
		return new ArrayList<SqlParameter>(params).get(i);
	}

}
