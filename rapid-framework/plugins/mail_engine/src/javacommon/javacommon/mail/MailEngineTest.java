package javacommon.mail;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import cn.org.rapid_framework.util.concurrent.async.IResponder;

public class MailEngineTest extends TestCase {
	MailEngine engine = new MailEngine();

	
	
	public void setUp()throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-mail.xml");
		engine = (MailEngine)context.getBean("mailEngine");
	}
	
	public void tearDown() throws Exception{
		engine.destroy();
	}
	
	public void testSend() throws Exception {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("badqiu@gmail.com");
		msg.setFrom("rapidframework@126.com");
		msg.setText("hello: badqiu, <h1>2008</h1>");
		msg.setSubject("test subject");
		
		AsyncToken token = engine.sendAsyncMsg(msg);
		
		token.addResponder(new IResponder() {
			public void onFault(Exception e) {
				System.out.println("send email fail,cause:"+e);
				e.printStackTrace();
			}

			public void onResult(Object result) {
				System.out.println("send email success");
			}
		});
		
		Thread.sleep(1000 * 5);
	}
	
}
