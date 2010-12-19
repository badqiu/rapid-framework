package cn.org.rapid_framework.mail;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.util.Assert;

/**
 * 邮件消息创建的工具类
 * @author badqiu
 *
 */
public class SimpleMailMessageUtils {
		
	/**
	 * 创建html类型的邮件
	 */
	public static HtmlMimeMessagePreparator toHtmlMsg(final SimpleMailMessage msg) {
		return toHtmlMsg(msg,null);
	}
	
	/**
	 * 创建html类型的邮件
	 * @param msg 邮件消息
	 * @param fromPersonal 发件人的名称
	 * @return
	 */
	public static HtmlMimeMessagePreparator toHtmlMsg(final SimpleMailMessage msg, final String fromPersonal) {
		return new HtmlMimeMessagePreparator(msg,fromPersonal);
	}
	
	public static class HtmlMimeMessagePreparator implements MimeMessagePreparator{
		private SimpleMailMessage simpleMailMessage;
		private String fromPersonal;
		
		public HtmlMimeMessagePreparator(SimpleMailMessage simpleMailMessage) {
			this(simpleMailMessage,null);
		}
		
		public HtmlMimeMessagePreparator(SimpleMailMessage simpleMailMessage,String fromPersonal) {
			super();
			setSimpleMailMessage(simpleMailMessage);
			this.fromPersonal = fromPersonal;
		}
		
		public SimpleMailMessage getSimpleMailMessage() {
			return simpleMailMessage;
		}

		public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
			Assert.notNull(simpleMailMessage,"simpleMailMessage must be not null");
			this.simpleMailMessage = simpleMailMessage;
		}

		public String getFromPersonal() {
			return fromPersonal;
		}

		public void setFromPersonal(String fromPersonal) {
			this.fromPersonal = fromPersonal;
		}

		public void prepare(MimeMessage mimeMessage) throws Exception {
			simpleMailMessage.copyTo(new MimeMailMessage(mimeMessage));
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,mimeMessage.getEncoding());
			helper.setText(simpleMailMessage.getText(),true);
			
			if(StringUtils.isNotEmpty(fromPersonal)) {
				mimeMessage.setFrom(new InternetAddress(simpleMailMessage.getFrom(),fromPersonal));
			}
		}
	}

}
