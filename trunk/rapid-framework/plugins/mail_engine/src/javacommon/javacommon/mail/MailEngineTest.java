package javacommon.mail;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;

import cn.org.rapid_framework.mail.AsyncJavaMailSender;
import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import cn.org.rapid_framework.util.concurrent.async.IResponder;

public class MailEngineTest extends TestCase {
	MailEngine engine;
	AsyncJavaMailSender asyncMailSender;
	
	
	public void setUp()throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-mail.xml");
		engine = (MailEngine)context.getBean("mailEngine");
		asyncMailSender = (AsyncJavaMailSender)context.getBean("asyncJavaMailSender");
	}
	
	public void tearDown() throws Exception{
	}
	
	public void testSend() throws Exception {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("badqiu@gmail.com");
		msg.setFrom("badqiu@126.com");
		msg.setText("hello: badqiu, <h1>2008</h1>");
		msg.setSubject("test subject");
		
		AsyncToken token = asyncMailSender.send(msg);
		
		token.addResponder(new IResponder() {
			public void onFault(Exception e) {
				System.out.println("send email fail,cause:"+e);
				e.printStackTrace();
			}

			public void onResult(Object result) {
				System.out.println("send email success");
			}
		});
		
		token = engine.sendHtmlMail(msg, "中文badqiu");
		token.addResponder(new IResponder() {
			public void onFault(Exception e) {
				System.out.println("send email fail,cause:"+e);
				e.printStackTrace();
			}

			public void onResult(Object result) {
				System.out.println("send email success,中文badqiu");
			}
		});
		System.out.println("sleep 5 seconds");
		Thread.sleep(1000 * 5);
	}
	
}
