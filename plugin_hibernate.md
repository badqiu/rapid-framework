

# hibernate插件说明 #

现hibernate默认使用Annotation配置,你也可以切换使用hibernate xml配置.

# 使用说明 #
修改hibernate的配置文件applicationContext-dao.xml
```
1. sessionFactory bean的修改
	* packagesToScan属性:           修改包名以确定要加载的@Entity class
	* hibernateProperties属性: 	根据你使用的数据库修改的hibernate.dialect
2. 修改context:component-scan节点以确定要加载的dao类
```

# hibernate配置文件applicationContext-dao.xml内容 #
```
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
    http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd"
    default-autowire="byName" default-lazy-init="false">

	<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>

	<!--Hibernate Annotation SessionFatory-->
	<bean id="sessionFactory" class="org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean">
		<property name="dataSource" ref="dataSource"/>
		<!--<property name="mappingLocations" value="classpath*:com/creawor/demo/model/*.hbm.xml"/>-->
		
		<!-- packagesToScan可以自动搜索某个package的全部标记@Entity class -->
		<property name="packagesToScan">
			<list>
				<value>com.**.model</value>
			</list>
		</property>
		<property name="hibernateProperties">
			<props>
				<!--常用数据库方言 MySQL5Dialect,SQLServerDialect,OracleDialect,SybaseDialect,DB2Dialect -->
				<prop key="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</prop>
				<prop key="hibernate.show_sql">true</prop>
				<prop key="hibernate.query.substitutions">true 1, false 0</prop>
				<prop key="hibernate.default_batch_fetch_size">4</prop>
			</props>
		</property>
	</bean>
	
	<!--Hibernate TransactionManager-->
	<bean id="transactionManager" class="org.springframework.orm.hibernate3.HibernateTransactionManager">
		<property name="sessionFactory" ref="sessionFactory"/>
	</bean>
	
	<!-- component-scan自动搜索@Component , @Controller , @Service , @Repository等标注的类 -->
	<context:component-scan base-package="com.**.dao"/>
	
</beans>
```

# 切换使用hibernate hbm.xml配置 #
  1. 切换生成器模板至hibernate xml
    1. 手工将template/other/java\_hibernate\_xml的文件覆盖template/java\_src的内容
  1. 修改applicationContext-dao.xml
    1. 为sessionFactory.mappingLocations设置需要加载的hbm.xml
    1. 将sessionFactory.packagesToScan属性删除