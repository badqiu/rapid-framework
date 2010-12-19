package cn.org.rapid_framework.ibatis3.plugin;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Test;

import cn.org.rapid_framework.jdbc.dialect.DerbyDialect;
import cn.org.rapid_framework.jdbc.dialect.Dialect;
import cn.org.rapid_framework.jdbc.dialect.MySQLDialect;
import cn.org.rapid_framework.jdbc.dialect.OracleDialect;
import cn.org.rapid_framework.jdbc.dialect.SQLServerDialect;



public class OffsetLimitInterceptorTest {
	
	OffsetLimitInterceptor di = new OffsetLimitInterceptor();
	
	@Test(timeout=4500)
	public void preformance_processIntercept() throws Throwable {
		testWithDialect(new MySQLDialect(), RowBounds.NO_ROW_OFFSET,RowBounds.NO_ROW_LIMIT,"select * from userinfo limit 100,200");
		testWithDialect(new OracleDialect(),RowBounds.NO_ROW_OFFSET,RowBounds.NO_ROW_LIMIT, "select * from ( select row_.*, rownum rownum_ from ( select * from userinfo ) row_ ) where rownum_ <= 100+200 and rownum_ > 100");
		testWithDialect(new SQLServerDialect(),100,RowBounds.NO_ROW_LIMIT, "select top 200 * from userinfo");
		testWithDialect(new DerbyDialect(),100,200, "select * from userinfo ");
	}

	private void testWithDialect(Dialect dialect,int expectedOffset,int expectedLimit, String expctedSql) {
		di.dialect = dialect;
		Configuration conf = new Configuration();
		
		////queryArgs = query(MappedStatement ms, Object parameter, int offset, int limit, ResultHandler resultHandler)
		
		int count = 10000 * 10;
		long start = System.currentTimeMillis();
		for(int i = 0; i < count; i++) {
			Builder builder = new MappedStatement.Builder(conf,"id",new StaticSqlSource(conf,"select * from userinfo "),SqlCommandType.SELECT);
			MappedStatement ms = builder.build();
			Object[] args = new Object[]{ms,new Object(),new RowBounds(100,200),null};
			di.processIntercept(args);
			
			MappedStatement newMs = (MappedStatement)args[0];
			BoundSql sql = newMs.getBoundSql(null);
			RowBounds rowBounds = (RowBounds)args[2];
			int offset = rowBounds.getOffset();
			int limit = rowBounds.getLimit();
			Assert.assertEquals(expectedOffset,offset);
			Assert.assertEquals(expectedLimit,limit);
			Assert.assertEquals(expctedSql,sql.getSql());
		}
		float cost = System.currentTimeMillis() - start;
		
		System.out.println(String.format("costTime:%s perMethodCost:%s tps:%s %s",cost,cost/count,count/(cost/1000),dialect.getClass().getSimpleName()));
	}
}
