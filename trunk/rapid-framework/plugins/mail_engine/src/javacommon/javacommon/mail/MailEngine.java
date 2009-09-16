package javacommon.mail;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import cn.org.rapid_framework.mail.AsyncJavaMailSender;
import cn.org.rapid_framework.util.concurrent.async.AsyncToken;

public class MailEngine {
	protected static final Log log = LogFactory.getLog(MailEngine.class);
	
	private AsyncJavaMailSender asyncJavaMailSender;
	
	public void setAsyncJavaMailSender(AsyncJavaMailSender asyncJavaMailSender) {
		this.asyncJavaMailSender = asyncJavaMailSender;
	}

	/**
	 * 异步发送邮件
	 * @param msg
	 * @return
	 */
	public AsyncToken sendAsyncMsg(final SimpleMailMessage msg) {
		return asyncJavaMailSender.send(new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage)
					throws Exception {
				MimeMailMessage mimeMailMessage = new MimeMailMessage(mimeMessage);
				msg.copyTo(mimeMailMessage);
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				helper.setText(msg.getText(),true);
			}
		});
	}
	
}
