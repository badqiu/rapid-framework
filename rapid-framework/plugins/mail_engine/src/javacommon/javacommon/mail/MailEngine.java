package javacommon.mail;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.Assert;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken;

public class MailEngine implements InitializingBean,DisposableBean{
	protected static final Log log = LogFactory.getLog(MailEngine.class);
	
	private int sendMailThreadPool = 0;
	ExecutorService executorService; //邮件发送的线程池
	private JavaMailSenderImpl mailSender = null;
	
	public void setExecutorService(ExecutorService executor) {
		this.executorService = executor;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
	
	public void setSendMailThreadPool(int sendMailThreadPool) {
		this.sendMailThreadPool = sendMailThreadPool;
	}
	
	public void afterPropertiesSet() throws Exception {
		if(executorService == null && sendMailThreadPool > 0) {
			executorService = Executors.newFixedThreadPool(sendMailThreadPool,new CustomizableThreadFactory("MailEngine-"));
			log.info("create send mail executorService,threadPoolSize:"+sendMailThreadPool);
		}
		
		Assert.notNull(mailSender,"mailSender must be not null");
		Assert.notNull(executorService,"executorService must be not null");
	}
	
	public void destroy() throws Exception {
		List<Runnable> noExecuteTasks = executorService.shutdownNow();
		
		if(noExecuteTasks.size() > 0) {
			log.warn("send mail executorService shutdowned,unexecute task size:"+noExecuteTasks.size());
		}else {
			log.info("send mail executorService shutdowned");
		}
	}
	
	/**
	 * 异步发送邮件
	 * @param msg
	 * @return
	 */
	public AsyncToken sendAsyncMsg(final SimpleMailMessage msg) {
		final AsyncToken asyncToken = new AsyncToken();
		executorService.execute(new Runnable() {
			public void run() {
				try {
					log.info("Send Email,subject:"+msg.getSubject()+" address="+StringUtils.join(msg.getTo()));
					mailSender.send(new MimeMessagePreparator() {
						public void prepare(MimeMessage mimeMessage)
								throws Exception {
							MimeMailMessage mimeMailMessage = new MimeMailMessage(mimeMessage);
							msg.copyTo(mimeMailMessage);
							MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
							helper.setText(msg.getText(),true);
						}
					});
					asyncToken.setComplete();
				} catch (Exception ex) {
					log.warn("Send Email Fail,error cause:"+ex.getMessage());
					asyncToken.setFault(ex);
				}
			}
		});
		return asyncToken;
	}
	
}
