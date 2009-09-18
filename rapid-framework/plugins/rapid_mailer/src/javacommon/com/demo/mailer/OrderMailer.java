package com.demo.mailer;

import java.util.HashMap;
import java.util.Map;

import javacommon.mail.BaseMailer;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import cn.org.rapid_framework.mail.SimpleMailMessageUtils;
import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import cn.org.rapid_framework.util.concurrent.async.AsyncTokenCallback;
import cn.org.rapid_framework.util.concurrent.async.IResponder;

/**
 * 本类的演示邮件发送,请删除本类.
 * 
 * <pre>
 * Mailer使用规范:
 * 1. 包名: 放在mailer包内,如com.company.project.mailer
 * 2. 类名: 以Mailer结尾,如UserMailer
 * 3. 方法名: 
 * 			使用UserMailer.createXXXX()来创建邮件消息,如UserMailer.createConfirmMail()
 * 			使用UserMailer.sendXXXX()来发送邮件,如UserMailer.sendConfirmMail()
 * 4.必须继承之BaseMailer
 * 5.单元测试一般情况下测试testCreateXXXX()即可
 * </pre>
 * 
 * @author badqiu
 */
@Component
public class OrderMailer extends BaseMailer{
	
	/**
	 * 使用freemarker模板创建邮件消息
	 */
	public MimeMessagePreparator createConfirmOrder(String username) {
		SimpleMailMessage msg = newSimpleMsgFromTemplate("subject");
		msg.setTo("badqiu@gmail.com");
		
		final Map model = new HashMap();
		model.put("username", username);
		String text = getFreemarkerTemplateProcessor().processTemplate("confirmOrder.flt", model);
		msg.setText(text);
		
		//转换为html邮件,另有一个参数可以指定发件人名称
		return SimpleMailMessageUtils.toHtmlMsg(msg);
	}
	
	/**
	 * 发送邮件
	 */
	public AsyncToken sendConfirmOrder(final String username) {
		final MimeMessagePreparator msg = createConfirmOrder(username);
		AsyncToken token = getAsyncJavaMailSender().send(msg);
		
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

	/**
	 * 演示使用AsyncTokenTemplate发送邮件
	 */
	public void sendConfirmOrder2(final String username) {
		// AsyncTokenTemplate可以指定默认需要添加的IResponder,请查看AsyncTokenTemplate.setResponders()方法
		AsyncToken token = getAsyncTokenTemplate().execute(new AsyncTokenCallback() {
			public AsyncToken execute() {
				final MimeMessagePreparator msg = createConfirmOrder(username);
				return getAsyncJavaMailSender().send(msg);
			}
		});
		
		//处理邮件发送结果
		token.addResponder(new IResponder() {
			public void onFault(Exception fault) {
				System.out.println("[ERROR] confirmOrder mail send fail,cause:"+fault);
			}
			public void onResult(Object result) {
				System.out.println("[INFO] confirmOrder mail send success");
			}
		});
	}
	
}
