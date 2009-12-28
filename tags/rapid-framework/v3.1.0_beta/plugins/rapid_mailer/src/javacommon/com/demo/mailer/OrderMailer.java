package com.demo.mailer;

import java.util.HashMap;
import java.util.Map;

import javacommon.mail.BaseMailer;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import cn.org.rapid_framework.mail.SimpleMailMessageUtils;
import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import cn.org.rapid_framework.util.concurrent.async.IResponder;

/**
 * 本类为演示邮件发送,请删除本类.
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
	public SimpleMailMessage createConfirmOrder(String username) {
		SimpleMailMessage msg = newSimpleMsgFromTemplate("测试邮件subject");
		msg.setTo("badqiu@gmail.com");
		
		final Map model = new HashMap();
		model.put("username", username);
		String text = processTemplate("confirmOrder.flt", model);
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
