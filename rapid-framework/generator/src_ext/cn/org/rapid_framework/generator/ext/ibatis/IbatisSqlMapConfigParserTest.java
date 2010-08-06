package cn.org.rapid_framework.generator.ext.ibatis;

import java.util.HashMap;

import junit.framework.TestCase;

public class IbatisSqlMapConfigParserTest extends TestCase {
	
	HashMap<String,String> hashMap = new HashMap();
	IbatisSqlMapConfigParser parser = new IbatisSqlMapConfigParser();
	public void setUp() {
		hashMap.put("question", "username = ? and password = ?");
		hashMap.put("placeholder", "sex = #equals_sex# and password = #pwd#");
	}
	
	@SuppressWarnings("static-access")
	public void test_iterate_open_and_close() {
		assertEquals("  (#abc123[]#  ) ",parser.parse("<iterate open='(' close=')' conjunction='OR' property='orIncludeAges'>#abc123[]# </iterate>"));
		assertEquals("  (#abc123[]#  ) ",IbatisSqlMapConfigParser.parse("<iterate open='(' close=')' conjunction=',' property='orIncludeAges'>#abc123[]# </iterate>"));
		assertEquals("  (#abc123[]# )",parser.parse("<iterate open='(' close=')' conjunction='OR' property='orIncludeAges'>#abc123[]#"));
	}

	public void test_iterate_open() {
		assertEquals("  (#abc123[]#   ",parser.parse("<iterate open='(' conjunction='OR' property='orIncludeAges'>#abc123[]# </iterate>"));
		assertEquals("  #abc123[]#  ) ",IbatisSqlMapConfigParser.parse("<iterate close=')' conjunction=',' property='orIncludeAges'>#abc123[]# </iterate>"));
		assertEquals("  #abc123[]# ",parser.parse("<iterate property='orIncludeAges'>#abc123[]#"));
	}
	
	public void test_include_by_refid() {
		assertEquals(" username = ? and password = ? ",parser.parse("<include refid='question'/>",hashMap));
		assertEquals(" sex = #equals_sex# and password = #pwd# ",parser.parse("<include refid='placeholder'/>",hashMap));
		assertEquals(" begin__sex = #equals_sex# and password = #pwd# end ",parser.parse("begin__<include refid='placeholder'/> end",hashMap));
		assertEquals(" begin__sex = #equals_sex# and password = #pwd# end ",parser.parse("begin__<include refid='placeholder'><include/> end",hashMap));
		assertEquals(" begin__sex = #equals_sex# and password = #pwd# inner text  end ",parser.parse("begin__<include refid='placeholder'> inner text <include/> end",hashMap));
		
		try {
		assertEquals(" username = ? and password = ? ",parser.parse("<include refid='question'/>",new HashMap()));
		fail();
		}catch(IllegalArgumentException e) {
			
		}
	}
	
	public void test_prepend() {
		assertEquals("  AND age = ?  ",parser.parse("<isNotEmpty prepend='AND' property='age'>age = ?</isNotEmpty>"));
		assertEquals("  sex! age = ?  ",parser.parse("<isNotEmpty prepend='sex!' property='age'>age = ?</isNotEmpty>"));
		assertEquals("  age = ?  ",parser.parse("<isNotEmpty property='age'>age = ?</isNotEmpty>"));
	}
	
	public void test_xml_escaped() {
		assertEquals("  3&2<1> age = ?  ",parser.parse("<isNotEmpty prepend='3&amp;2&lt;1&gt;' property='age'>age = ?</isNotEmpty>"));
		assertEquals("  3&2<1> age &lt; ?  ",parser.parse("<isNotEmpty prepend='3&amp;2&lt;1&gt;' property='age'>age &lt; ?</isNotEmpty>"));
		assertEquals("  3&2<1> age &gt;= ?  ",parser.parse("<isNotEmpty prepend='3&amp;2&lt;1&gt;' property='age'>age &gt;= ?</isNotEmpty>"));
	}

	public void test_remove_where() {
		assertEquals("  WHERE 1=1  ",parser.parse("<isNotEmpty prepend='where' property='age'>and 1=1</isNotEmpty>"));
		assertEquals("  WHERE 1=1  ",parser.parse("<isNotEmpty prepend='wHere' property='age'>And 1=1</isNotEmpty>"));
		assertEquals("  WHERE 1=1  ",parser.parse("<isNotEmpty prepend='whERe' property='age'>AnD 1=1</isNotEmpty>"));
		assertEquals(" select * from user  whERe 1=1  ",parser.parse("select * from user <isNotEmpty prepend='whERe' property='age'>1=1</isNotEmpty>"));
		assertEquals(" select * from user WHERE 1=1 ",parser.parse("select * from user where and 1=1"));
		assertEquals(" select * from user\nWHERE 1=1 ",parser.parse("select * from user\nwhere \n\nand 1=1"));
	}

	public void test_remove_comments() {
		assertEquals(" and 1=1  ",parser.parse("<!--<isNotEmpty prepend='where' property='age'>\n-->and 1=1</isNotEmpty>"));
		assertEquals(" And 1=1  ",parser.parse("/*<isNotEmpty prepend='wHere' property='age'>\n*/And 1=1</isNotEmpty>"));
		assertEquals("  whERe   ",parser.parse("<isNotEmpty prepend='whERe' property='age'>query not allowed</isNotEmpty>"));
	}
	
	public void test_include_by_refid_will_empty() {
		assertEquals("  ",parser.parse("<include />",hashMap));
		
		assertEquals("多了前后空格是错误的","sex = #equals_sex# and password = #pwd#",parser.parse("<include refid='placeholder'/>",hashMap));
	}
	
}
