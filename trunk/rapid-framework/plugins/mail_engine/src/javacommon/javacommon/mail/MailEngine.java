package javacommon.mail;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	 * 
	 * @param msg
	 * @param fromPersonal 发件人
	 * @return
	 */
	public AsyncToken sendHtmlMail(final SimpleMailMessage msg,final String fromPersonal) {
		return asyncJavaMailSender.send(new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage)
					throws Exception {
				MimeMailMessage mimeMailMessage = new MimeMailMessage(mimeMessage);
				msg.copyTo(mimeMailMessage);
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				helper.setText(msg.getText(),true);
				mimeMessage.setFrom(new InternetAddress(msg.getFrom(),fromPersonal));
			}
		});
	}

}
