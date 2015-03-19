# Spring @Transaction标注声明事务 #

现默认使用spring提供的@Transaction标注来声明事务管理。

事务的开启在下面的配置文件中:

applicationContext-resources.xml
```
<!-- 支持 @Transactional 标记 -->
	<tx:annotation-driven transaction-manager="transactionManager" proxy-target-class="true"/>
```

#### service层代码 ####
```
@Transactional
public class UserInfoManager extends BaseManager<UserInfo,java.lang.Long>{
        //.... 

	@Transactional(readOnly=true)
	public Page findByPageRequest(PageRequest pr) {
		return userInfoDao.findByPageRequest(pr);
	}
	
}
```



---

# Spring 

&lt;aop:config /&gt;

事务 #

你也可以根据喜好，继续使用

&lt;aop:config /&gt;

 

&lt;tx:advice /&gt;

来配置事务

配置文件在:

applicationContext-resources.xml

# 配置内容 #
```
	<!-- 以AspectJ方式 定义 AOP -->
	<aop:config proxy-target-class="true">
		<aop:advisor pointcut="execution(* javacommon.base.BaseManager.*(..))" advice-ref="txAdvice"/>
		<aop:advisor pointcut="execution(* com.*..*.service.*Manager.*(..))" advice-ref="txAdvice"/>
	</aop:config>

	<!-- 基本事务定义,使用transactionManager作事务管理,默认get* find*方法的事务为readonly,其余方法按默认设置.
			 默认的设置请参考Spring文档事务一章. -->
	<tx:advice id="txAdvice" transaction-manager="transactionManager">
		<tx:attributes>
			<tx:method name="find*" read-only="true"/>
			<tx:method name="get*" read-only="true"/>
			<tx:method name="*" read-only="false"/>
		</tx:attributes>
	</tx:advice>
```