
# 介绍 #

rapid扩展了iBatis使其也可以使用类似hiberante提供的[jdbc分页方言](rapid_dialect.md).

而如何扩展iBatis,rapid是依照这篇文章:http://pengfeng.javaeye.com/blog/200772,改造而来.

iBatis的SqlExecutor负责sql的最终执行，所以在此处扩展出LimitSqlExecutor,以便结合方言使用数据库物理分页.
```
public class LimitSqlExecutor extends SqlExecutor {

	Dialect dialect;

	@Override
	public void executeQuery(StatementScope statementScope, Connection conn, String sql, Object[] parameters, int skipResults, int maxResults, RowHandlerCallback callback) throws SQLException {
		if (supportsLimit() && (skipResults != NO_SKIPPED_RESULTS || maxResults != NO_MAXIMUM_RESULTS)) {
			sql = sql.trim();
			if(dialect.supportsLimitOffset()) {
				sql = dialect.getLimitString(sql, skipResults, maxResults);
				skipResults = NO_SKIPPED_RESULTS;
			}else {
				sql = dialect.getLimitString(sql, 0, maxResults);
			}
			maxResults = NO_MAXIMUM_RESULTS;
		}
		super.executeQuery(statementScope, conn, sql, parameters, skipResults, maxResults, callback);
	}


}
```

然后在SqlMapClient创建时为其设置我们的limitSqlExecutor. 此处我们扩展了spring的SqlMapClientFactoryBean.

```
/**
 * 用于生成SqlMapClient,但增加设置sqlExecutor属性,以便用于扩展ibatis使用数据库物理分页
 * @author badqiu
 *
 */
public class SqlMapClientFactoryBean extends org.springframework.orm.ibatis.SqlMapClientFactoryBean{
	private SqlExecutor sqlExecutor;
	
	public SqlExecutor getSqlExecutor() {
		return sqlExecutor;
	}

	public void setSqlExecutor(SqlExecutor sqlExecutor) {
		this.sqlExecutor = sqlExecutor;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		SqlMapClient c = (SqlMapClient)getObject();
		if(sqlExecutor != null && c instanceof SqlMapClientImpl) {
			SqlMapClientImpl client = (SqlMapClientImpl)c;
			SqlMapExecutorDelegate delegate = client.getDelegate();
			try {
				ReflectUtil.setFieldValue(delegate, "sqlExecutor", SqlExecutor.class, sqlExecutor);
				System.out.println("[iBATIS] success set ibatis SqlMapClient.sqlExecutor = "+sqlExecutor.getClass().getName());
			}catch(Exception e) {
				System.out.println("[iBATIS] error,cannot set ibatis SqlMapClient.sqlExecutor = "+sqlExecutor.getClass().getName()+" cause:"+e);
			}
		}
	}
}
```

其后在使用SqlMapClient时即可使用物理分页
```
List list = getSqlMapClientTemplate().queryForList(statementName, parameterObject,offset,limit)
```

# 存在的问题 #
现ibatis2.3,ibatis3的分页参数没有使用疑问号 **占位符"?"** ,所以在如oracle这种数据库,会影响性能.


spring\_jdbc现在的实现没有这个问题