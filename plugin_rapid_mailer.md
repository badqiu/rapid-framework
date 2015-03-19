# 邮件插件:Mailer #

本插件模仿之ruby on rails的actionmailer,但有提升,请查看特性.

本插件适用于一般小型项目的邮件发送需求

本插件rapid-framework-v3.0发布

# Mailer特性 #
  1. 使用AsyncJavaMailSender启动线程池可以异步发送邮件并通过[AsyncToken](http://badqiu.javaeye.com/blog/461089)得到发送结果。
  1. 使用freemarker模板语言生成邮件内容

# Mailer使用规范 #
```
 1. 包名: 放在mailer包内,如com.company.project.mailer
 2. 类名: 以Mailer结尾,如UserMailer
 3. 方法名: 
 	使用UserMailer.createXXXX()来创建邮件消息,如UserMailer.createConfirmMail(args)
 	使用UserMailer.sendXXXX()来发送邮件,如UserMailer.sendConfirmMail(args)
 4.必须继承之BaseMailer(注：BaseMailer只是组合各个组件，并未包含其它功能)
 5.单元测试一般情况下测试testCreateXXXX()即可
```


# 邮件发送使用实例 #
```
@Component
public class OrderMailer extends BaseMailer{
	/**
	 * 使用freemarker模板创建邮件消息
	 */
	public SimpleMailMessage createConfirmOrder(String username) {
		SimpleMailMessage msg = newSimpleMsgFromTemplate("测试邮件subject");
		msg.setTo("badqiu(a)gmail.com");
		
		final Map model = new HashMap();
		model.put("username", username);
		String text = getFreemarkerTemplateProcessor().processTemplate("confirmOrder.flt", model);
		msg.setText(text);
		
		return msg;
	}
	
	/**
	 * 发送邮件
	 */
	public AsyncToken sendConfirmOrder(final String username) {
		final SimpleMailMessage msg = createConfirmOrder(username);
		
		//转换为html邮件并发送,另有一个参数可以指定发件人名称
		AsyncToken token = getAsyncJavaMailSender().send(SimpleMailMessageUtils.toHtmlMsg(msg,"rapid小明")); 
		
		//处理邮件发送结果
		token.addResponder(new IResponder() {
			public void onFault(Exception fault) {
				System.out.println("[ERROR] confirmOrder mail send fail,cause:"+fault);
			}
			public void onResult(Object result) {
				System.out.println("[INFO] confirmOrder mail send success");
			}
		});
		
		//返回token可以用于外部继续监听
		return token;
	}
}
```

## 配置文件applicationContext-mail.xml ##
```
<beans>
	<!-- 邮件消息模板,指定一些邮件的默认值 -->
	<bean id="simpleMailMessageTemplate" class="org.springframework.mail.SimpleMailMessage">
		<property name="from" value="rapidframework@sohu.com"></property>
	</bean>
	
	<!-- 正常的普通邮件发送器 -->
	<bean id="javaMailSender" class="org.springframework.mail.javamail.JavaMailSenderImpl">
		<property name="host" value="smtp.sohu.com" />
		<property name="username" value="rapidframework@sohu.com" />
		<property name="password" value="123456" />
		<property name="defaultEncoding" value="UTF-8" />
		
		<property name="javaMailProperties">
			<props>
				<prop key="mail.smtp.auth">true</prop>
			</props>
		</property>
	</bean>

	<!-- 异步的邮件发送器 -->
	<bean id="asyncJavaMailSender" class="cn.org.rapid_framework.mail.AsyncJavaMailSender">
		<!-- 邮件发送的线程池大小 -->
		<property name="sendMailThreadPoolSize" value="3" />
		<property name="javaMailSender" ref="javaMailSender" />
		<property name="asyncTokenFactory" ref="mailerAsyncTokenFactory"></property>
    </bean>
    
    <!-- AsyncTokenFactory可以为AsyncToken指定默认的responders -->
	<bean id="mailerAsyncTokenFactory" class="cn.org.rapid_framework.util.concurrent.async.DefaultAsyncTokenFactory">
		<property name="responders">
			<list></list>
		</property>
	</bean>

	<!-- freemarker模板相关配置-->	
	<bean id="freemarkerTemplateProcessor" class="cn.org.rapid_framework.freemarker.FreemarkerTemplateProcessor">
		<property name="configuration">
			<bean class="org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean">
				<property name="templateLoaderPath" value="classpath:/email_template" />
				<property name="defaultEncoding" value="UTF-8"/>
			</bean>
		</property>
	</bean>
	
	<!-- component-scan自动搜索@Component , @Controller , @Service , @Repository等标注的类 -->
	<context:component-scan base-package="com.**.mailer"/>
	
</beans>
```