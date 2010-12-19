package cn.org.rapid_framework.cache.aop.interceptor;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;
import cn.org.rapid_framework.cache.aop.annotation.MethodCache;

public class AnnotationMethodCacheInterceptorTest extends TestCase {

	public void test_getCacheKeyWithArguments_with_null() {
		String key = new AnnotationMethodCacheInterceptor().createCacheKey("UserInfo.join({args},{args})", new String[0]);
		assertEquals("UserInfo.join(,)",key);
		
		key = new AnnotationMethodCacheInterceptor().createCacheKey("UserInfo.join({args},{args})", null);
		assertEquals("UserInfo.join(null,null)",key);
		
		
	}
	
	public void test_getCacheKeyWithArguments() {
		String key = new AnnotationMethodCacheInterceptor().createCacheKey("UserInfo.join({args})", new String[]{"1","2"});
		assertEquals("UserInfo.join(1,2)",key);
		
		
		key = new AnnotationMethodCacheInterceptor().createCacheKey("UserInfo.join({args},{args})", new String[]{"1","2"});
		assertEquals("UserInfo.join(1,2,1,2)",key);
		
		key = new AnnotationMethodCacheInterceptor().createCacheKey("UserInfo.join(%s)", new String[]{"1","2"});
		assertEquals("UserInfo.join(1)",key);
		
		key = new AnnotationMethodCacheInterceptor().createCacheKey("UserInfo.join(%s  %s)", new String[]{"1","2"});
		assertEquals("UserInfo.join(1  2)",key);
		
		key = new AnnotationMethodCacheInterceptor().createCacheKey("UserInfo.join(%s  %s {3})", new String[]{"1","2"});
		assertEquals("UserInfo.join(1  2 {3})",key);
		
		key = new AnnotationMethodCacheInterceptor().createCacheKey("UserInfo.join(%s  %s {3} %1$s)", new String[]{"1","2"});
		assertEquals("UserInfo.join(1  2 {3} 1)",key);
	}
	
	public void test_getCacheKey() {
		String key = new AnnotationMethodCacheInterceptor().createDefaultCacheKey("UserManager","getById", new String[]{"1"});
		assertEquals("UserManager.getById(1)",key);
		
		key = new AnnotationMethodCacheInterceptor().createDefaultCacheKey("UserManager","getById", new String[]{"1","2"});
		assertEquals("UserManager.getById(1,2)",key);
		
		key = new AnnotationMethodCacheInterceptor().createDefaultCacheKey("UserManager","getById", null);
		assertEquals("UserManager.getById()",key);
		
		key = new AnnotationMethodCacheInterceptor().createDefaultCacheKey("UserManager","getById", new String[0]);
		assertEquals("UserManager.getById()",key);
		
		key = new AnnotationMethodCacheInterceptor().createDefaultCacheKey("UserManager","getById", new String[2]);
		assertEquals("UserManager.getById(,)",key);
		
		key = new AnnotationMethodCacheInterceptor().createDefaultCacheKey("UserManager","getById", new Object[]{1,new String[]{"11","12"}});
		assertEquals("UserManager.getById(1,[11,12])",key);
		
		key = new AnnotationMethodCacheInterceptor().createDefaultCacheKey("UserManager","getById", new Object[]{1,new String[]{"11",null,null,"14"}});
		assertEquals("UserManager.getById(1,[11,,,14])",key);
		
		key = new AnnotationMethodCacheInterceptor().createDefaultCacheKey("UserManager","getById", new Object[]{"1","2",3,4});
		assertEquals("UserManager.getById(1,2,3,4)",key);
		
	}
	
	public void test_format() {
		assertEquals("1 2 3",String.format("%s %s %s", "1",2,3L));
		
		assertEquals("1 2 3",String.format("%s %s %s",  "1",2,3L));
		
		assertEquals("123",String.format("%0$s",  "123",2,3L));
		assertEquals("123 123",String.format("%0$s %1$s",  "123",2,3L));
		assertEquals("2 3",String.format("%2$s %3$s",  "123",2,3L));
		assertEquals("123 123 2 123 3",String.format("%0$s %1$s %2$s %1$s %3$s",  "123",2,3L));
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		assertEquals("Duke's Birthday: 2010",String.format("Duke's Birthday: %1$tY", c));
	}
	
	public void test_cache_key_prefix() {
	    AnnotationMethodCacheInterceptor interceptor = new AnnotationMethodCacheInterceptor();
	    interceptor.setCacheKeyPrefix("AppBadqiu__");
	    MethodCache annotation = new MethodCache(){
            public String cacheKey() {
                return null;
            }

            public long expireTime() {
                return 0;
            }

            public TimeUnit timeUnit() {
                return null;
            }

            public Class<? extends Annotation> annotationType() {
                return null;
            }
	    };
	    
        String key = interceptor.getCackeKey("User", "say", new Integer[]{1,2},annotation);
        assertEquals("AppBadqiu__User.say(1,2)",key);
        
        key = interceptor.getCackeKey("User", "say", new Integer[]{1,2}, new MethodCache(){
            public String cacheKey() {
                return "key";
            }

            public long expireTime() {
                return 0;
            }

            public TimeUnit timeUnit() {
                return null;
            }

            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });
        assertEquals("AppBadqiu__key",key);
	}
}
