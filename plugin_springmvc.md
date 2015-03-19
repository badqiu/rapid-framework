# SpringMVC #

springmvc本身有一个ControllerClassNameHandlerMapping类可以实现零配置,生成的URL规则为/pathPrefix/class/method.do. rapid对这个类做了小改造,以支持生成的URL区分大小写.

以下为实际的配置段
```
<!-- 
	生成spring的URL映射,spring的ControllerClassNameHandlerMapping可以生成"/pathPrefix/entity/method.do"这样的URL映射,
	但为了项目统一URL,使用rapid的ControllerClassNameHandlerMapping可以生成"/pathPrefix/Entity/method.do"
	rapid: cn.org.rapid_framework.spring.web.servlet.mvc.support.ControllerClassNameHandlerMapping 
	spring: org.springframework.web.servlet.mvc.support.ControllerClassNameHandlerMapping
-->
<bean class="cn.org.rapid_framework.spring.web.servlet.mvc.support.ControllerClassNameHandlerMapping" >
	<!-- <property name="caseSensitive" value="true"/> -->
	<!-- 前缀可选 -->
	<property name="pathPrefix" value="/pages"></property>
</bean>
```

其它的再没有任何修改,都是使用spring的原生机制.凭你个人喜好,如果喜欢spring原生的小写开头的URL,则修改使用spring的ControllerClassNameHandlerMapping即可.