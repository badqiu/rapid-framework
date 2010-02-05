package cn.org.rapid_framework.generator.provider.db.model;

import org.junit.Test;

import cn.org.rapid_framework.beanutils.BeanUtils;


public class ColumnTest {
	
	@Test
	public void testColumn() {
		Column c = new Column(new Table(),1,"int","user_name",1,2,true,true,true,true,"","remarks");
		System.out.println(BeanUtils.describe(c));
	}
}
