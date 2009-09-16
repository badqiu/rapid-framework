package cn.org.rapid_framework.mail;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken;

/**
 * 邮件消息创建的工具类
 * @author badqiu
 *
 */
public class MailMessageUtils {
		
	/**
	 * 创建html类型的邮件
	 */
	public static MimeMessagePreparator createHtmlMsg(final SimpleMailMessage msg) {
		return createHtmlMsg(msg,null);
	}
	
	/**
	 * 创建html类型的邮件
	 * @param msg 邮件消息
	 * @param fromPersonal 发件人的名称
	 * @return
	 */
	public static MimeMessagePreparator createHtmlMsg(final SimpleMailMessage msg, final String fromPersonal) {
		return new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage)
					throws Exception {
				MimeMailMessage mimeMailMessage = new MimeMailMessage(mimeMessage);
				msg.copyTo(mimeMailMessage);
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				helper.setText(msg.getText(),true);
				
				if(StringUtils.isNotEmpty(fromPersonal)) {
					mimeMessage.setFrom(new InternetAddress(msg.getFrom(),fromPersonal));
				}
			}
		};
	}

}
