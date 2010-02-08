package cn.org.rapid_framework.generator.provider.db.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;



public class ColumnTest {
	
	@Test
	public void testColumn() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Column c = new Column(new Table(),1,"int","user_name",1,2,true,true,true,true,"","remarks");
		System.out.println(BeanUtils.describe(c));
	}
}
