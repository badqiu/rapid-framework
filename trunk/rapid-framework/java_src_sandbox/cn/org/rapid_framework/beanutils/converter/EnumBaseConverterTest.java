package cn.org.rapid_framework.beanutils.converter;

import junit.framework.TestCase;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;

import cn.org.rapid_framework.lang.enums.EnumBase;
import cn.org.rapid_framework.lang.enums.EnumBaseUtils;
import cn.org.rapid_framework.util.fortest_enum.SomeTypeEnum;

public class EnumBaseConverterTest extends TestCase{

	public void test_convert_success() {
		assertEquals(SomeTypeEnum.K1,new EnumBaseConverter().convert(SomeTypeEnum.class, "K1"));
		assertEquals(SomeTypeEnum.K2,new EnumBaseConverter().convert(SomeTypeEnum.class, "K2"));
		assertEquals(null,new EnumBaseConverter().convert(SomeTypeEnum.class, ""));
		assertEquals(null,new EnumBaseConverter().convert(SomeTypeEnum.class, null));
		
		assertEquals("K1",new EnumBaseConverter().convert(String.class, SomeTypeEnum.K1));
		assertEquals("K2",new EnumBaseConverter().convert(String.class, SomeTypeEnum.K2));
		assertEquals(null,new EnumBaseConverter().convert(String.class, null));	
		
		
		try {
			assertEquals(null,new EnumBaseConverter().convert(Integer.class, SomeTypeEnum.K1));	
			fail();
		}catch(ConversionException e) {
		}
	}
	
	public void test_convert_fail() {

		try {
		assertEquals(null,new EnumBaseConverter().convert(SomeTypeEnum.class, " "));	
		fail();
		}catch(ConversionException e) {
		}
			
		try {
		assertEquals(null,new EnumBaseConverter().convert(String.class, ""));	
		fail();
		}catch(ClassCastException e) {
		}
	}
	
	public void test_convert_by_ConvertUtils() {
		ConvertUtils.register(new EnumBaseConverter(), EnumBase.class);
		ConvertUtils.register(new EnumBaseConverter(), String.class);
		ConvertUtils.register(new EnumBaseConverter(), Integer.class);
		ConvertUtils.register(new EnumBaseConverter(), Long.class);
		
		assertEquals("K1",ConvertUtils.convert(SomeTypeEnum.K1));
		assertEquals("K2",ConvertUtils.convert(SomeTypeEnum.K2));
		
		try {
		assertEquals("K1",ConvertUtils.convert(SomeTypeEnum.K1,Integer.class));
		fail();
		}catch(ConversionException e) {
		}
	}
}
