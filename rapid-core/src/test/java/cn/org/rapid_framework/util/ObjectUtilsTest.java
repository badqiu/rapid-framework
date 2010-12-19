package cn.org.rapid_framework.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class ObjectUtilsTest {
	HashMap NOT_EMPTY_MAP = new HashMap();
	@Before
	public void setUp() {
		NOT_EMPTY_MAP.put("1", 1);
	}
	@Test
	public void testIsEmpty() {
		Assert.assertTrue(ObjectUtils.isEmpty(null));
		Assert.assertTrue(ObjectUtils.isEmpty(""));
		Assert.assertTrue(ObjectUtils.isEmpty(new ArrayList()));
		Assert.assertTrue(ObjectUtils.isEmpty(new HashMap()));
		Assert.assertTrue(ObjectUtils.isEmpty(new Object[]{}));
		
		Assert.assertTrue(!ObjectUtils.isEmpty(" "));
		Assert.assertTrue(!ObjectUtils.isEmpty(Arrays.asList(1,2,3)));
		Assert.assertTrue(!ObjectUtils.isEmpty(NOT_EMPTY_MAP));
		Assert.assertTrue(!ObjectUtils.isEmpty(new Object[]{1,2,3}));
	}
	
	@Test
	public void testIsEmptyWithIeelgalArgument() {
		Assert.assertFalse(ObjectUtils.isEmpty(new Object()));
	}
}
