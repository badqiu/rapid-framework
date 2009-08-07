package cn.org.rapid_framework.generator.util;

import cn.org.rapid_framework.generator.util.StringHelper;
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
		
		assertEquals("level1_channel",StringHelper.toUnderscoreName("LEVEL1_CHANNEL"));
		assertEquals("Level1Channel",StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName("LEVEL1_CHANNEL")));
		
	}
}
