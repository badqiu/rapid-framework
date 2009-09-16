package com.demo.mailer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javacommon.mail.BaseMailer;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import cn.org.rapid_framework.mail.MailMessageUtils;
import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import cn.org.rapid_framework.util.concurrent.async.IResponder;
import freemarker.template.TemplateException;

@Component
public class OrderMailer extends BaseMailer{
	
	public SimpleMailMessage createConfirmOrder(String username) throws TemplateException, IOException {
		SimpleMailMessage msg = newSimpleMsgFromTemplate();
		msg.setSubject(wrapSubject("subject"));
		msg.setTo("badqiu@gmail.com");
		
		Map model = new HashMap();
		model.put("username", username);
		
		msg.setText(processWithTemplate(model, "confirmOrder.flt"));
		return msg;
	}

	public void sendConfirmOrder(String username) throws TemplateException, IOException {
		SimpleMailMessage msg = createConfirmOrder(username);
		AsyncToken token = asyncJavaMailSender.send(MailMessageUtils.createHtmlMsg(msg));
		
		//处理邮件发送结果
		token.addResponder(new IResponder() {
			public void onFault(Exception fault) {
				System.out.println("confirmOrder mail send fail,cause:"+fault);
			}
			public void onResult(Object result) {
				System.out.println("confirmOrder mail send success");
			}
		});
	}
	
}
