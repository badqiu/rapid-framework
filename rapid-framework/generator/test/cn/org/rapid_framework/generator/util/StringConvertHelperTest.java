package cn.org.rapid_framework.generator.util;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.provider.db.model.Column.EnumMedatada;


public class StringConvertHelperTest extends  TestCase{
	
	public void test_string2Map() {
		Map map = StringConvertHelper.string2Map("abc=123,diy='123',sex='blog'");
		System.out.println(map.keySet()+" "+map.values());
		assertEquals("123",map.get("abc"));
		assertEquals("'123'",map.get("diy"));
		assertEquals("'blog'",map.get("sex"));
		
		assertEquals(map.size(),3);
		
		assertTrue(StringConvertHelper.string2Map(null).isEmpty());
	}
	
	public void test_string2ColumnEnumList() {
		List<EnumMedatada> list= StringConvertHelper.string2ColumnEnumList("F(1,女);M(0,男)");
		assertEquals(2,list.size());
		EnumMedatada f= list.get(0);
		assertEquals(f.getEnumAlias(),"F");
		assertEquals(f.getEnumDesc(),"女");
		assertEquals(f.getEnumKey(),"1");
		
		EnumMedatada m= list.get(1);
		assertEquals(m.getEnumAlias(),"M");
		assertEquals(m.getEnumDesc(),"男");
		assertEquals(m.getEnumKey(),"0");
	}
	
	public void test_string2ColumnEnumList_with_exception() {
		try {
		List<EnumMedatada> list= StringConvertHelper.string2ColumnEnumList(",,");
		fail();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}
