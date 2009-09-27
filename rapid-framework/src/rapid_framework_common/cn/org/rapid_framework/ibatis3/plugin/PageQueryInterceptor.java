package cn.org.rapid_framework.ibatis3.plugin;



import java.util.Properties;

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
import org.springframework.util.ReflectionUtils;

import cn.org.rapid_framework.jdbc.dialect.Dialect;
import cn.org.rapid_framework.util.PropertiesHelper;

@Intercepts({@Signature(
		type= Executor.class,
		method = "query",
		args = {MappedStatement.class, Object.class, int.class, int.class, ResultHandler.class})})
public class PageQueryInterceptor implements Interceptor{
	static int MAPPED_STATEMENT_INDEX = 0;
	static int PARAMETER_INDEX = 1;
	static int SKIP_RESULTS_INDEX = 2;
	static int MAX_RESULTS_INDEX = 3;
	static int RESULT_HANDLER_INDEX = 4;
	
	Dialect dialect;
	
	public Object intercept(Invocation invocation) throws Throwable {
		processIntercept(invocation.getArgs());
		return invocation.proceed();
	}

	void processIntercept(final Object[] queryArgs) {
		//queryArgs = query(MappedStatement ms, Object parameter, int offset, int limit, ResultHandler resultHandler)
		MappedStatement ms = (MappedStatement)queryArgs[MAPPED_STATEMENT_INDEX];
		Object parameter = queryArgs[PARAMETER_INDEX];
		final int offset = (Integer)queryArgs[SKIP_RESULTS_INDEX];
		final int limit = (Integer)queryArgs[MAX_RESULTS_INDEX];
		
		if(dialect.supportsLimit() && (offset != Executor.NO_ROW_OFFSET || limit != Executor.NO_ROW_LIMIT)) {
			BoundSql boundSql = ms.getBoundSql(parameter);
			String sql = boundSql.getSql().trim();
			if (dialect.supportsLimitOffset()) {
				sql = dialect.getLimitString(sql, offset, limit);
				queryArgs[MAX_RESULTS_INDEX] = Executor.NO_ROW_LIMIT;
			} else {
				sql = dialect.getLimitString(sql, 0, limit);
			}
			queryArgs[SKIP_RESULTS_INDEX] = Executor.NO_ROW_OFFSET;
			
			BoundSql newBoundSql = new BoundSql(sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
			MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
			queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
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
		String dialectClass = new PropertiesHelper(properties).getRequiredString("dialectClass");
		try {
			dialect = (Dialect)Class.forName(dialectClass).newInstance();
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		} 
		System.out.println(PageQueryInterceptor.class.getSimpleName()+".dialect="+dialectClass);
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
	
}
