package cn.org.rapid_framework.ibatis3;



import java.util.Properties;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.result.ResultHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.util.ReflectionUtils;

import cn.org.rapid_framework.jdbc.dialect.Dialect;
import cn.org.rapid_framework.jdbc.dialect.MySQLDialect;

@Intercepts({@Signature(
		type= Executor.class,
		method = "query",
		args = {MappedStatement.class, Object.class, int.class, int.class, ResultHandler.class})})
public class DialectInterceptor implements Interceptor{
	
	static Dialect dialect = new MySQLDialect();
	static int MAPPED_STATEMENT_INDEX = 0;
	static int PARAMETER_INDEX = 1;
	static int SKIP_RESULTS_INDEX = 2;
	static int MAX_RESULTS_INDEX = 3;
	static int RESULT_HANDLER_INDEX = 4;
	
	public Object intercept(Invocation invocation) throws Throwable {
		
		
		final Object[] queryArgs = invocation.getArgs();
		processIntercept(queryArgs);
		
		return invocation.proceed();
	}

	void processIntercept(final Object[] queryArgs)
			throws ClassNotFoundException {
		//queryArgs = query(MappedStatement ms, Object parameter, int offset, int limit, ResultHandler resultHandler)
		MappedStatement ms = (MappedStatement)queryArgs[MAPPED_STATEMENT_INDEX];
		Object parameter = queryArgs[PARAMETER_INDEX];
		final int offset = (Integer)queryArgs[SKIP_RESULTS_INDEX];
		final int limit = (Integer)queryArgs[MAX_RESULTS_INDEX];
		
		if(dialect.supportsLimit() && (offset != Executor.NO_ROW_OFFSET || limit != Executor.NO_ROW_LIMIT)) {
//			SqlSource newSqlSource = createSqlSourceProxy(queryArgs, ms.getSqlSource(),offset, limit);
//			MappedStatement newMs = copyFromMappedStatement(ms, newSqlSource);
			final BoundSql sql = ms.getBoundSql(parameter);
			MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(sql));
			queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
		}
	}
	
	public static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;
		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}
		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}

	SqlSource createSqlSourceProxy(final Object[] queryArgs,SqlSource sqlSource, final int offset, final int limit)throws ClassNotFoundException {
		ProxyFactoryBean factory = new ProxyFactoryBean();
		factory.setTarget(sqlSource);
		factory.addAdvice(new SqlSousrceMethodInterceptor(queryArgs, offset, limit));
		factory.setProxyInterfaces(new Class[]{SqlSource.class});
		SqlSource newSqlSource = (SqlSource)factory.getObject();
		return newSqlSource;
	}
	
	static class SqlSousrceMethodInterceptor implements MethodInterceptor {
		Object[] queryArgs;
		int offset;
		int limit;
		
		public SqlSousrceMethodInterceptor(Object[] queryArgs, int offset,int limit) {
			super();
			this.queryArgs = queryArgs;
			this.offset = offset;
			this.limit = limit;
		}

		public Object invoke(MethodInvocation mi) throws Throwable {
			Object result = mi.proceed();
			BoundSql boundSql = (BoundSql)result;
			String sql = boundSql.getSql().trim();
	        if(dialect.supportsLimitOffset()) {
	        	sql = dialect.getLimitString(sql, offset, limit);
	        	queryArgs[MAX_RESULTS_INDEX] = Executor.NO_ROW_LIMIT;
	        }else {
	        	sql = dialect.getLimitString(sql, 0, limit);
	        }
	        queryArgs[SKIP_RESULTS_INDEX] = Executor.NO_ROW_OFFSET;
	        return new BoundSql(sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
		}		
	}

	private MappedStatement copyFromMappedStatement(MappedStatement ms,SqlSource newSqlSource) {
		Builder builder = new MappedStatement.Builder(ms.getConfiguration(),ms.getId(),newSqlSource,ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		builder.keyProperty(ms.getKeyProperty());
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(ms.getResultMaps());
		builder.cache(ms.getCache());
		MappedStatement newMs = builder.build();
		return newMs;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		String dialectClass = getRequiredProperty(properties,"dialectClass");
		try {
			dialect = (Dialect)Class.forName(dialectClass).newInstance();
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		} 
		System.out.println(DialectInterceptor.class.getSimpleName()+".dialect="+dialectClass);
	}
	
	private static String getRequiredProperty(Properties p,String key) {
		String v = p.getProperty(key);
		if(v == null || v.trim().length() == 0) {
			throw new IllegalArgumentException("not found required property with key="+key);
		}
		return v;
	}
	
}
