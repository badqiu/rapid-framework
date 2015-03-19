

# 配置说明 #

iBatis通过SqlMapClientFactoryBean创建SqlMapClient,可以指定分页方言(该项为rapid扩展)

## SqlMapClientFactoryBean配置说明 ##
```
    <!-- rapid SqlMapClientFactoryBean与spring SqlMapClientFactoryBean的区别是可以指定sqlExecutor -->
    <bean id="sqlMapClient" class="cn.org.rapid_framework.ibatis.spring.SqlMapClientFactoryBean">
        <property name="configLocation">
            <value>classpath:sql-map-config.xml</value>
        </property>
        <property name="mappingLocations">
        	<value>classpath*:/com/**/model/**/*SqlMap.xml</value>
        </property>
        <property name="dataSource" ref="dataSource"/>
        
        <!-- 指定数据库分页方言Dialect, 其它方言:OracleDialect,SQLServerDialect,SybaseDialect,DB2Dialect,PostgreSQLDialect,MySQLDialect,DerbyDialect-->
        <property name="sqlExecutor">
        	<bean class="cn.org.rapid_framework.ibatis.sqlmap.engine.execution.LimitSqlExecutor">
        		<property name="dialect">
        			<bean class="cn.org.rapid_framework.jdbc.dialect.MySQLDialect"/>
        		</property>
        	</bean>
        </property>
    </bean>
```

## 以下为具体修改说明. ##
```
	1.修改配置文件: src/resource/spring/applicationContext-dao.xml
		1.1修改dialect,以支持不同的数据库分页查询. 默认值为:MySQLDialect (注:该项为rapid的扩展)
	3.模板主键生成策略修改,修改${className}SqlMap.xml中的<selectKey>节点,默认是使用mysql 的auto_increment
```

# iBatis分页使用方言(Dialect) #
iBatis的使用方言的分页改进请查看[ibatis分页改进](rapid_ibatis.md)

# 不使用方言进行分页 #
1.修改sqlmap.xml,直接使用数据库的分页.
```
    <!-- 分页查询将传 #offset#,#pageSize#,#lastRows# 三个参数,不同的数据库可以根于此三个参数属性应用不同的分页实现,默认为mysql -->
    <select id="pageSelect" resultMap="userinfoResult">
	    <![CDATA[
	    select
        	id ,
        	username ,
        	age ,
        	birth_date ,
        	nickName 
	        from userinfo 
	    ]]>
		limit #offset#,#pageSize#		    
    </select>
```
2.查询时,传递offset,pageSize等参数
```
	protected Page pageQuery(String statementName, PageRequest pageRequest) {
		
		Number totalCount = (Number) this.getSqlMapClientTemplate().queryForObject(getCountQuery(),pageRequest.getFilters());
		Page page = new Page(pageRequest,totalCount.intValue());
		
		//其它分页参数,用于不喜欢或是因为兼容性而不使用方言(Dialect)的分页用户使用. 与getSqlMapClientTemplate().queryForList(statementName, parameterObject)配合使用
		Map otherFilters = new HashMap();
		otherFilters.put("offset", page.getFirstResult());
		otherFilters.put("pageSize", page.getPageSize());
		otherFilters.put("lastRows", page.getFirstResult() + page.getPageSize());
		otherFilters.put("sortColumns", pageRequest.getSortColumns());
		
		//混合两个filters为一个filters,MapAndObject.get()方法将在两个对象取值,Map如果取值为null,则再在Bean中取值
		Map parameterObject = new MapAndObject(otherFilters,pageRequest.getFilters());
		List list = getSqlMapClientTemplate().queryForList(statementName, parameterObject);
		page.setResult(list);
		return page;
	}
```