package cn.org.rapid_framework.generator.util;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.provider.db.table.model.Column.EnumMetaDada;


public class StringConvertHelperTest extends  TestCase{
	
//	public void test_string2Map() {
//		Map map = StringHelper.string2Map("abc=123,diy='123',sex='blog'");
//		System.out.println(map.keySet()+" "+map.values());
//		assertEquals("123",map.get("abc"));
//		assertEquals("'123'",map.get("diy"));
//		assertEquals("'blog'",map.get("sex"));
//		
//		assertEquals(map.size(),3);
//		
//		assertTrue(StringHelper.string2Map(null).isEmpty());
//	}
	
	public void test_string2ColumnEnumList_with_three_argument() {
		List<EnumMetaDada> list= StringHelper.string2EnumMetaData("F(1,女);M(0,男)");
		verify3Argument(list);
		
		list= StringHelper.string2EnumMetaData("F(1,女),M(0,男)");
        verify3Argument(list);
        
        list= StringHelper.string2EnumMetaData("F(1,女),M(2,男),G(3,未知)");
        assertEquals(3,list.size());
        verifyMetadata(list.get(0), "F", "女", "1");
        verifyMetadata(list.get(1), "M", "男", "2");
        verifyMetadata(list.get(2), "G", "未知", "3");
	}

    private void verify3Argument(List<EnumMetaDada> list) {
        assertEquals(2,list.size());
        EnumMetaDada f= list.get(0);
		verifyMetadata(f, "F", "女", "1");
		
		EnumMetaDada m= list.get(1);
		verifyMetadata(m, "M", "男", "0");
    }
	
	
	public void test_string2ColumnEnumList_with_two_argument() {
		List<EnumMetaDada> list= StringHelper.string2EnumMetaData("F(女);M(男)");
		verify2Argument(list);
		list= StringHelper.string2EnumMetaData("F(女),M(男)");
		verify2Argument(list);
		
	    list= StringHelper.string2EnumMetaData("F(女),M(男);G(未知)");
	    assertEquals(3,list.size());
	    verifyMetadata(list.get(0), "F", "女", "F");
	    verifyMetadata(list.get(1), "M", "男", "M");
	    verifyMetadata(list.get(2), "G", "未知", "G");
	}

	public void testEmptyString() {
        assertTrue(StringHelper.string2EnumMetaData("").isEmpty());
		assertTrue(StringHelper.string2EnumMetaData("  ").isEmpty());
		assertTrue(StringHelper.string2EnumMetaData(null).isEmpty());
    }

    private void verify2Argument(List<EnumMetaDada> list) {
        assertEquals(2,list.size());
		EnumMetaDada f= list.get(0);
		verifyMetadata(f, "F", "女", "F");
		
		EnumMetaDada m= list.get(1);
		verifyMetadata(m, "M", "男", "M");
    }

    private void verifyMetadata(EnumMetaDada m, String enumAlias, String enumDesc, String enumKey) {
        assertEquals(m.getEnumAlias(),enumAlias);
		assertEquals(m.getEnumDesc(),enumDesc);
		assertEquals(m.getEnumKey(),enumKey);
    }
	
	public void test_string2ColumnEnumList_with_exception() {
		try {
		List<EnumMetaDada> list= StringHelper.string2EnumMetaData(",,");
		fail();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}
