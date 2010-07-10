package com.demo.mailer;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import freemarker.template.TemplateException;

/**
 * 本类为演示邮件发送,请删除本类.
 * 
 * @author badqiu
 *
 */
public class OrderMailerTest {
	OrderMailer orderMailer;
	
	@Before
	public void setUp()throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-mail.xml");
		orderMailer = (OrderMailer)context.getBean("orderMailer");
	}
	
	@Test
	public void testSendFromOrderMailer() throws TemplateException, IOException, Exception {
		AsyncToken token = orderMailer.sendConfirmOrder("badqiu");
		
		//waitForResult主要用于测试，请使用相同功能的Future.get()
		token.waitForResult();
	}
	
}
