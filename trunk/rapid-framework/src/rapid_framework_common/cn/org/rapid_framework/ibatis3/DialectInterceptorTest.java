package cn.org.rapid_framework.ibatis3;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.executor.result.ResultHandler;
import org.apache.ibatis.mapping.Configuration;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.plugin.Invocation;
import org.junit.Test;


public class DialectInterceptorTest {
	
	DialectInterceptor di = new DialectInterceptor();
	
	@Test(timeout=4000)
	public void preformance_createSqlSourceProxy() throws ClassNotFoundException {
		int count = 10000 * 10;
		long start = System.currentTimeMillis();
		for(int i = 0; i < count; i++) {
			Object[] args = new Object[4];
			di.createSqlSourceProxy(args, new StaticSqlSource("select * from userinfo group by badqiu"), 100, 300);
		}
		long cost = System.currentTimeMillis() - start;
		
		System.out.println(String.format("costTime:%s perMethodCost:%s",cost,cost/(double)count));
	}
	
	@Test(timeout=500)
	public void preformance_processIntercept() throws Throwable {
		Configuration conf = new Configuration();
		
		////queryArgs = query(MappedStatement ms, Object parameter, int offset, int limit, ResultHandler resultHandler)
		
		int count = 10000 * 10;
		long start = System.currentTimeMillis();
		for(int i = 0; i < count; i++) {
			Builder builder = new MappedStatement.Builder(conf,null,new StaticSqlSource("select * from userinfo group by badqiu"),null);
			MappedStatement ms = builder.build();
			Object[] args = new Object[]{ms,new Object(),100,200,null};
			di.processIntercept(args);
		}
		long cost = System.currentTimeMillis() - start;
		
		System.out.println(String.format("costTime:%s perMethodCost:%s",cost,cost/(double)count));
	}
}
