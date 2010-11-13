package cn.org.rapid_framework.cache.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)     
@Retention(RetentionPolicy.RUNTIME)     
public @interface MethodCache {  
	/** 
	 * 过期时间,单位为秒
	 **/
    int expireTime();  
    
    /** 
     * 存放在cache中的key,支持String.format() 语法.
     * 
     * <pre>
     * 示例:
     * 有一个 UserManager.queryBy(String username,String password);
     * 1. String.format()格式: @MethodCache(cacheKey="UserManager.queryBy(%s,%s)")
     * 2. {args}特殊格式: @MethodCache(cacheKey="UserManager.queryBy({args})")
     * </pre>
     **/
    String cacheKey() default "";
}
