package cn.org.rapid_framework.generator.provider.db.model;

import org.junit.Test;

import cn.org.rapid_framework.beanutils.BeanUtils;


public class TableTest {
	
	@Test
	public void table() {
		Table t = new Table();
		t.setSqlName("user_info");
		System.out.println(BeanUtils.describe(t));
	}
}
