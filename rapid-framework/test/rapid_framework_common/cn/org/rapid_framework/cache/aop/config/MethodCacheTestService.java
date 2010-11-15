package cn.org.rapid_framework.cache.aop.config;

import cn.org.rapid_framework.cache.aop.annotation.MethodCache;

public class MethodCacheTestService {
	
	public int count = 100;
	public int notCacheCount = 100;
	public int ageCount = 100;
	@MethodCache(expireTime=100)
	public int queryByUser(String username,String password) {
		return ++count;
	}
	
	public int notCachedqueryByUser(String username,String password) {
		return ++notCacheCount;
	}
	
	@MethodCache(expireTime=100,cacheKey="Service.queryByUser(%s,%s,%s)")
    public int queryByUser(String username,String password,int age) {
        return ++ageCount;
    }
}
