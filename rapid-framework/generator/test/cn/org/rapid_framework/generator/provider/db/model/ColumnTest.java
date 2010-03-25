package cn.org.rapid_framework.generator.provider.db.model;

import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.util.BeanHelper;



public class ColumnTest extends TestCase{
	
	public void testColumn() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Column c = new Column(new Table(),1,"int","user_name",1,2,true,true,true,true,"","remarks");
		System.out.println(BeanHelper.describe(c));
	}
}
