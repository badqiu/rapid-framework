package com.demo.mailer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;

import cn.org.rapid_framework.mail.AsyncJavaMailSender;
import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import cn.org.rapid_framework.util.concurrent.async.IResponder;
import freemarker.template.TemplateException;

public class OrderMailerTest extends TestCase {
	OrderMailer orderMailer;
	
	public void setUp()throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-mail.xml");
		orderMailer = (OrderMailer)context.getBean("orderMailer");
	}
	
	public void testSendFromOrderMailer() throws TemplateException, IOException, InterruptedException {
		Map model = new HashMap();
		model.put("username", "badqiu");
		orderMailer.sendConfirmOrder(model);
		
		Thread.sleep(1000 * 5);
	}
	
}
