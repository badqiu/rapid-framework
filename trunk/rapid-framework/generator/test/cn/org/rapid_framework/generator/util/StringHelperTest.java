package cn.org.rapid_framework.generator.util;

import java.util.StringTokenizer;

import cn.org.rapid_framework.generator.util.StringHelper;
//import cn.org.rapid_framework.util.ProfileUtils;
import junit.framework.TestCase;
/**
 * @author badqiu
 */
public class StringHelperTest extends TestCase {
	public void testToUnderscoreName() {
		assertEquals(null,StringHelper.toUnderscoreName(null));
		assertEquals("",StringHelper.toUnderscoreName(""));
		assertEquals(" ",StringHelper.toUnderscoreName(" "));
		assertEquals(" u",StringHelper.toUnderscoreName(" u"));
		assertEquals("a",StringHelper.toUnderscoreName("A"));
		
		assertEquals("user",StringHelper.toUnderscoreName("User"));
		
		assertEquals("user_foo",StringHelper.toUnderscoreName("userFoo"));
		assertEquals("user_foo",StringHelper.toUnderscoreName("UserFoo"));
		assertEquals("user_foo_bar",StringHelper.toUnderscoreName("userFooBar"));
		
		assertEquals("_user",StringHelper.toUnderscoreName("_user"));
		assertEquals("user_foo_bar_foo_b_ar",StringHelper.toUnderscoreName("user_foo_bar_Foo_BAr"));
		assertEquals("user_foo",StringHelper.toUnderscoreName("user_Foo"));
		assertEquals("user_foo_up",StringHelper.toUnderscoreName("user_FooUp"));
		assertEquals("user",StringHelper.toUnderscoreName("user"));
		
		assertEquals("user_foo_bar_user_info",StringHelper.toUnderscoreName("userFooBar_UserInfo"));
		
		assertEquals("user__info",StringHelper.toUnderscoreName("user__Info"));
		
		assertEquals("user_info",StringHelper.toUnderscoreName("USER_INFO"));
		assertEquals("user",StringHelper.toUnderscoreName("USER"));
		assertEquals("u_se_r",StringHelper.toUnderscoreName("uSeR"));
		assertEquals("user",StringHelper.toUnderscoreName("user"));
		assertEquals("中",StringHelper.toUnderscoreName("中"));
		assertEquals("中_se_r",StringHelper.toUnderscoreName("中SeR"));
		
		assertEquals("level1_channel",StringHelper.toUnderscoreName("LEVEL1_CHANNEL"));
		assertEquals("Level1Channel",StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName("LEVEL1_CHANNEL")));
	}

	public void testContains() {
		assertTrue(StringHelper.contains("badqiu", "adq"));
		assertTrue(StringHelper.contains("badqiu", "ADQ"));
		assertTrue(StringHelper.contains("badqiu", "abc","dqi"));
		assertFalse(StringHelper.contains(null,"blog"));
		assertFalse(StringHelper.contains("badqiu", "ddd","ggg"));
		
		try {
		String NULL = null;
		assertFalse(StringHelper.contains("bad",NULL));
		fail();
		}catch(Exception e) {}
	}
//	public void testPerformance() {
//		ProfileUtils.printCostTime("underscoreName", new Runnable() {
//			public void run() {
//				for(int i = 0; i < 1000000; i++ ) {
//					assertEquals("level1_channel",underscoreName("level1Channel"));
//				}
//			}
//		});
//		ProfileUtils.printCostTime("underscoreNameOld", new Runnable() {
//			public void run() {
//				for(int i = 0; i < 1000000; i++ ) {
//					assertEquals("level_1_channel",underscoreNameOld("level1Channel"));
//				}
//			}
//		});
//		
//		
//		System.out.println(underscoreName("level1channel"));
//	}
	
	private String underscoreName(String name) {
		StringBuffer result = new StringBuffer();
		if (name != null && name.length() > 0) {
			result.append(Character.toLowerCase(name.charAt(0)));
			for (int i = 1; i < name.length(); i++) {
				char s = name.charAt(i);
				if(Character.isDigit(s)) {
					result.append(s);
				}else 
				if (Character.isUpperCase(s)) {
					result.append("_");
					result.append(Character.toLowerCase(s));
				}
				else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}
	
	private String underscoreNameOld(String name) {
		StringBuffer result = new StringBuffer();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toLowerCase());
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals(s.toUpperCase())) {
					result.append("_");
					result.append(s.toLowerCase());
				}
				else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}
	
	public void test() {
		for(Object s : System.getProperties().keySet()) {
			System.out.println(s+"="+System.getProperty(s.toString()));
		}
	}
	
	public void testRemovePrefix() {
		assertEquals(null,StringHelper.removePrefix(null, "java.lang."));
		assertEquals("Integer",StringHelper.removePrefix("java.lang.Integer", "java.lang."));
	}
	
	public void test_remove_crlf() {
//		new StringTokenizer("\t\n\r\f")
		String[] array = StringHelper.tokenizeToStringArray("abc \r\f\n\t123\nbb", "\t\n\r\f");
		assertEquals("abc  123 bb",StringHelper.join(array," "));
//		assertEquals("abc 123","abc \n\t123".replace('\n', ' '));
	}
}
