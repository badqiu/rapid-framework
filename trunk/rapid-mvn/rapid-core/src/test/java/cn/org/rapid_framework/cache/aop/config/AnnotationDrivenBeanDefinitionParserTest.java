package cn.org.rapid_framework.cache.aop.config;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.org.rapid_framework.cache.Cache;
import edu.emory.mathcs.backport.java.util.Arrays;

public class AnnotationDrivenBeanDefinitionParserTest {
	
	ApplicationContext context = null;
	
	@Before
	public void setUp() {
		context = new ClassPathXmlApplicationContext("classpath:fortest_spring/test-methodCache.xml");
	}
	
	@Test
	public void test() {
		AnnotationDrivenBeanDefinitionParser a;
		Cache cache = (Cache)context.getBean("methodCache");
		assertEquals(1,cache.incr("click", 1));
		assertEquals(11,cache.incr("click", 10));
		assertEquals(-10,cache.decr("dbclick", 10));
		assertEquals(0,cache.incr("dbclick", 10));
	}
	
	@Test
	public void test_methodCacheTestService() {
		MethodCacheTestService methodCacheTestService = (MethodCacheTestService)context.getBean("methodCacheTestService");
		testMethodCacheTestServiceWithCache(methodCacheTestService);
	}
	
	@Test
	public void test_methodCacheTestServiceProxy() {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:fortest_spring/test-methodCache-proxyfactorybean.xml");
		
		MethodCacheTestService methodCacheTestService = (MethodCacheTestService)context.getBean("methodCacheTestServiceProxy");
		testMethodCacheTestServiceWithCache(methodCacheTestService);
	}

	private void testMethodCacheTestServiceWithCache(MethodCacheTestService methodCacheTestService) {
		assertEquals(101,methodCacheTestService.queryByUser("123", "456"));
		
		assertEquals(101,methodCacheTestService.queryByUser("123", "456"));
		
		assertEquals(102,methodCacheTestService.queryByUser("1234", "456"));
		
		assertEquals(101,methodCacheTestService.queryByUser("123", "456"));
		
		assertEquals(101,methodCacheTestService.notCachedqueryByUser("123", "456"));
		assertEquals(102,methodCacheTestService.notCachedqueryByUser("123", "456"));
		assertEquals(103,methodCacheTestService.notCachedqueryByUser("123", "456"));
		
		assertEquals(101,methodCacheTestService.cachedqueryByUser(new String[]{"123"}, Arrays.asList(new String[]{"456"})));
		assertEquals(101,methodCacheTestService.cachedqueryByUser(new String[]{"123"}, Arrays.asList(new String[]{"456"})));
		
		assertEquals(102,methodCacheTestService.cachedqueryByUser(new String[]{"123"}, Arrays.asList(new String[]{"4567"})));
		assertEquals(103,methodCacheTestService.cachedqueryByUser(new String[]{"456"}, Arrays.asList(new String[]{"456"})));
		assertEquals(101,methodCacheTestService.cachedqueryByUser(new String[]{"123"}, Arrays.asList(new String[]{"456"})));
		
		
	}
}
