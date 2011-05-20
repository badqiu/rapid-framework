package cn.org.rapid_framework.lang.enums;

import junit.framework.TestCase;
import cn.org.rapid_framework.util.fortest_enum.SomeTypeEnum;

public class EnumBaseUtilsTest extends TestCase {
	
	public void test() {
		SomeTypeEnum[] values = SomeTypeEnum.class.getEnumConstants();
		assertEquals(values[0],SomeTypeEnum.K1);
		assertEquals(values[1],SomeTypeEnum.K2);
	}
	
	public void test2() {
		SomeTypeEnum[] values = SomeTypeEnum.class.getEnumConstants();
		String str = EnumBaseUtils.getCode(SomeTypeEnum.K1);
		
	}
}
