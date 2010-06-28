package cn.org.rapid_framework.generator.provider.db.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.util.BeanHelper;



public class TableTest extends TestCase{
	
	public void testTable() throws Exception {
		GeneratorTestCase.runSqlScripts();
		
		Table t = TableFactory.getInstance().getTable("USER_INFO");

		print(t);
		
		System.out.println("\n\n column: \n");
		print(t.getColumns().iterator().next());
		printForTableConfig(t.getColumns().iterator().next());
	}

	private void print(Object o) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Map map = BeanHelper.describe(o);
		System.out.println("|| *属性* || *描述* || *示例值* ||");
		for(Object key : map.keySet()) {
			System.out.println(String.format("||*%s*|| ||%s||",key,map.get(key)));
		}
	}
	
	private void printForTableConfig(Column o) throws IllegalAccessException,InvocationTargetException, NoSuchMethodException {
		Map map = BeanHelper.describe(o);
		System.out.println("|| *属性* || *描述* || *示例值* ||");
		for(Object key : map.keySet()) {
			if(map.get(key) instanceof Boolean) {
				System.out.println(String.format("<%s>${c.%s?string}</%s>",key,key,map.get(key)));
			}else {
				System.out.println(String.format("<%s>${c.%s!}</%s>",key,key,map.get(key)));
			}
		}
	}
}
