package cn.org.rapid_framework.cache.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 用于方法缓存的标注.
 * 
 * 要使用本标注,需要通过在spring中开启配置.
 * <p>
 * &lt;method-cache:annotation-driven method-cache="methodCache" />	
 * </p>
 * 
 * <pre>
 * 示例使用:
 * 
 * public class UserManager {
 * 		&copy;MethodCache(expireTime=1000)
 * 		public void queryBy(String userName,String role) {
 *      }
 *      
 *      &copy;MethodCache(expireTime=1000,cacheKey="UserManager.queryBy(%s,%s,sex=%s)")
 * 		public void queryBy(String userName,String role,String sex) {
 *      }	
 *      
 *      &copy;MethodCache(expireTime=1000,cacheKey="UserManager.queryBy({args})")
 * 		public void queryBy(String userName,String role,int sex) {
 *      }     
 * }
 * </pre>
 * 
 * @author badqiu
 */
@Target(ElementType.METHOD)     
@Retention(RetentionPolicy.RUNTIME)     
public @interface MethodCache {  
	/** 
	 * 过期时间,时间单位通过timeUnit()指定,默认的时间单位为秒
	 **/
    long expireTime();  
    
    /** 
     * 过期时间的单位,默认为秒
     **/
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    
    /** 
     * 存放在cache中的key,支持String.format() 语法.
     * 
     * <pre>
     * 示例:
     * 有一个 UserManager.queryBy(String username,String password);
     * 1. String.format()格式: @MethodCache(cacheKey="UserManager.queryBy(%s,%s)")
     * 2. {args}特殊格式: @MethodCache(cacheKey="UserManager.queryBy({args})") {args}代表所有的参数值
     * </pre>
     **/
    String cacheKey() default "";
    
    
    //TODO 增加指定使用具体的cache对象. 如: Cache cache() default NULL;
    
}
