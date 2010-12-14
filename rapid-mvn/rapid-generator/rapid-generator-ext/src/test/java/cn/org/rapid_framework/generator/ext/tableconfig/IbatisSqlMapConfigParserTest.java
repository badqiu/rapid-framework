package cn.org.rapid_framework.generator.ext.tableconfig;

import java.util.HashMap;

import junit.framework.TestCase;

public class IbatisSqlMapConfigParserTest extends TestCase {
	
	HashMap<String,String> hashMap = new HashMap();
	IbatisSqlMapConfigParser parser = new IbatisSqlMapConfigParser();
	public void setUp() {
		hashMap.put("question", "username = ? and password = ?");
		hashMap.put("placeholder", "sex = #equals_sex# and password = #pwd#");
	}

	public void test_freemarker_xml() {
	    assertTrue("".equals(parser.removeComments("<!--1\n2\n3-->")));
	    assertTrue("".equals(parser.removeComments("/*1\n2\n3*/")));
	        
		stringEquals("#abc123[]# ",parser.parse("<#if databaseType?is_empty>#abc123[]# </#if>"));
		stringEquals("#abc123[]# ",parser.parse("<#if databaseType?is_empty>#abc123[]# </#if>"));
		stringEquals("#abc123[]# ",parser.parse("<@if databaseType?is_empty>#abc123[]# </@if>"));
	}
	
	public void test_remove_semicolon() {
	    assertEquals("select * from user_info",parser.parse("select * from user_info;"));
	    assertEquals("select * from user_info select * from user_info      ",parser.parse("select * from user_info; select * from user_info;      "));
	    assertEquals("select * from user_info &",parser.parse("select * from user_info &amp;"));
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

	
}
