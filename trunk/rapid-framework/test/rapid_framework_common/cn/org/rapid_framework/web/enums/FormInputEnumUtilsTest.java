package cn.org.rapid_framework.web.enums;

import java.util.Map;

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
}
