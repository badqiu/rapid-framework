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

import cn.org.rapid_framework.jdbc.dialect.MySQLDialect;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


public class DialectInterceptorTest {
	
	DialectInterceptor di = new DialectInterceptor();
	
	@Test(timeout=500)
	public void preformance_processIntercept() throws Throwable {
		di.dialect = new MySQLDialect();
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
