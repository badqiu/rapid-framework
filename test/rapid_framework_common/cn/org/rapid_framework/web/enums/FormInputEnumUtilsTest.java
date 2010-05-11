package cn.org.rapid_framework.web.enums;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Assert;
import org.junit.Test;


public class FormInputEnumUtilsTest {
	@Test
	public void test_toMap() {
		Map map = FormInputEnumUtils.toMap(RapidAreaEnum.values());
		Assert.assertEquals(2,map.size());
		Assert.assertTrue(map.containsKey("SH"));
		Assert.assertTrue(map.containsKey("GD"));
		Assert.assertTrue(map.containsValue("上海"));
		Assert.assertTrue(map.containsValue("广东"));
	}
	
	@Test
	public void test_toMap2() {
		Map map = FormInputEnumUtils.toMap(new RapidEnumUser("1","2"),new RapidEnumUser("2","2"));
		Assert.assertEquals(2,map.size());
		Assert.assertTrue(map.containsKey("1"));
		Assert.assertTrue(map.containsKey("2"));
		Assert.assertTrue(map.containsValue("2"));
	}
	
	@Test
	public void test_extractKeyValue() {
		Map map = FormInputEnumUtils.extractKeyValue("userid", "username", new RapidEnumUser("1","2"),new RapidEnumUser("2","2"));
		Assert.assertEquals(2,map.size());
		Assert.assertTrue(map.containsKey("1"));
		Assert.assertTrue(map.containsKey("2"));
		Assert.assertTrue(map.containsValue("2"));
	}
}
