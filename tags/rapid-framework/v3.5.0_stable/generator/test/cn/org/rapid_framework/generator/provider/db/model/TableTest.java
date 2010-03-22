package cn.org.rapid_framework.generator.provider.db.model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;

import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.provider.db.DbTableFactory;



public class TableTest extends TestCase{
	
	public void testTable() throws Exception {
		GeneratorTestCase.runSqlScripts();
		
		Table t = DbTableFactory.getInstance().getTable("USER_INFO");

		print(t);
		
		System.out.println("\n\n column: \n");
		print(t.getColumns().get(0));
	}

	private void print(Object o) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Map map = BeanUtils.describe(o);
		System.out.println("|| *属性* || *描述* || *示例值* ||");
		for(Object key : map.keySet()) {
			System.out.println(String.format("||%s|| ||%s||",key,map.get(key)));
		}
	}
}
