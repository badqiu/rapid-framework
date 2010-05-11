package cn.org.rapid_framework.generator.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.Enhancer;

import org.junit.Before;
import org.junit.Test;

import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.provider.db.model.Table;


public class MapBaseMethodInterceptorTest {
	
	Table source = null;
	HashMap map = new HashMap();
	Table table = (Table)createProxy(Table.class,source, map);
	@Before
	public void setUp() throws Exception {
		GeneratorTestCase.runSqlScripts();
		source = DbTableFactory.getInstance().getTable("USER_INFO");
	}
	@Test
	public void testTable() throws Exception {
        assertEquals(source.getSqlName(),table.getSqlName());
        assertEquals(source.getClassName(),table.getClassName());
        assertEquals("UserInfo",table.getClassName());
        
        map.put("className", "badqiu");
        assertEquals("badqiu",table.getClassName());
        
        map.put("className", "");
        assertEquals("UserInfo",table.getClassName());
        
        map.put("className", "1");
        assertEquals("1",table.getClassName());

        map.put("className", "  ");
        assertEquals("  ",table.getClassName());
        
        map.put("className", "blog");
        assertEquals("blog",table.getClassName());
	}

	@Test
	public void test_with_map_interface() throws Exception {
        assertEquals(source.getSqlName(),table.getSqlName());
        assertEquals(source.getClassName(),table.getClassName());
        assertEquals("UserInfo",table.getClassName());
        Map map = (Map)table;
        map.put("className", "badqiu");
        assertEquals("badqiu",map.get("className"));
        
        map.put("className", "");
        assertEquals("UserInfo",table.getClassName());
        
        map.put("className", "1");
        assertEquals("1",table.getClassName());

        map.put("className", "  ");
        assertEquals("  ",table.getClassName());
        
        map.put("className", "blog");
        assertEquals("blog",table.getClassName());
	}
	
	public static Object createProxy(Class clazz,Object target, HashMap map) {
		Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setInterfaces(new Class[]{Map.class});
		enhancer.setCallback(new MapBaseMethodInterceptor(map,target));
        return enhancer.create();
	}
}
