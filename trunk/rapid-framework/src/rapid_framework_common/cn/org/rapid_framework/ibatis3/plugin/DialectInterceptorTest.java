package cn.org.rapid_framework.ibatis3.plugin;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Configuration;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.junit.Assert;
import org.junit.Test;

import cn.org.rapid_framework.jdbc.dialect.Dialect;
import cn.org.rapid_framework.jdbc.dialect.MySQLDialect;
import cn.org.rapid_framework.jdbc.dialect.OracleDialect;


public class DialectInterceptorTest {
	
	DialectInterceptor di = new DialectInterceptor();
	
	@Test(timeout=1200)
	public void preformance_processIntercept() throws Throwable {
		testWithDialect(new MySQLDialect(), "select * from userinfo limit 100,200");
		testWithDialect(new OracleDialect(), "select * from ( select row_.*, rownum rownum_ from ( select * from userinfo ) row_ ) where rownum_ <= 300 and rownum_ > 100");
	}

	private void testWithDialect(Dialect dialect, String expcted) {
		di.dialect = dialect;
		Configuration conf = new Configuration();
		
		////queryArgs = query(MappedStatement ms, Object parameter, int offset, int limit, ResultHandler resultHandler)
		
		int count = 10000 * 10;
		long start = System.currentTimeMillis();
		for(int i = 0; i < count; i++) {
			Builder builder = new MappedStatement.Builder(conf,null,new StaticSqlSource("select * from userinfo "),null);
			MappedStatement ms = builder.build();
			Object[] args = new Object[]{ms,new Object(),100,200,null};
			di.processIntercept(args);
			
			MappedStatement newMs = (MappedStatement)args[0];
			BoundSql sql = newMs.getBoundSql(null);
			Assert.assertEquals(expcted,sql.getSql());
		}
		long cost = System.currentTimeMillis() - start;
		
		System.out.println(String.format("costTime:%s perMethodCost:%s",cost,cost/(double)count));
	}
}
