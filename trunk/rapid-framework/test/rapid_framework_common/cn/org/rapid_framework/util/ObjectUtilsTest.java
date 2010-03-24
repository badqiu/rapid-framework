package cn.org.rapid_framework.util;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import flex.messaging.io.ArrayList;


public class ObjectUtilsTest {
	HashMap NOT_EMPTY_MAP = new HashMap();
	@Before
	public void setUp() {
		NOT_EMPTY_MAP.put("1", 1);
	}
	@Test
	public void testIsEmpty() {
		Assert.isTrue(ObjectUtils.isEmpty(""));
		Assert.isTrue(ObjectUtils.isEmpty(new ArrayList()));
		Assert.isTrue(ObjectUtils.isEmpty(new HashMap()));
		Assert.isTrue(ObjectUtils.isEmpty(new Object[]{}));
		
		Assert.isTrue(!ObjectUtils.isEmpty(" "));
		Assert.isTrue(!ObjectUtils.isEmpty(Arrays.asList(1,2,3)));
		Assert.isTrue(!ObjectUtils.isEmpty(NOT_EMPTY_MAP));
		Assert.isTrue(!ObjectUtils.isEmpty(new Object[]{1,2,3}));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIsEmptyWithIeelgalArgument() {
		Assert.isTrue(ObjectUtils.isEmpty(new Object()));
	}
}
