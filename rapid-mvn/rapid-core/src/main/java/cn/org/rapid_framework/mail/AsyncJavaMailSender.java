package cn.org.rapid_framework.mail;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.Assert;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import cn.org.rapid_framework.util.concurrent.async.AsyncTokenFactory;
import cn.org.rapid_framework.util.concurrent.async.AsyncTokenUtils;
import cn.org.rapid_framework.util.concurrent.async.DefaultAsyncTokenFactory;

/**
 * 使用线程池异步发送邮件的javaMailSender
 * 每一个发送方法返回AsyncToken用于监听邮件是否发送成功
 * 
 * @see AsyncToken
 * @author badqiu
 *
 */
public class AsyncJavaMailSender implements InitializingBean,DisposableBean,BeanNameAware{
	protected static final Log log = LogFactory.getLog(AsyncJavaMailSender.class);
	
	protected int sendMailThreadPoolSize = 0;
	protected ExecutorService executorService; //邮件发送的线程池
	protected JavaMailSender javaMailSender;
	protected boolean shutdownExecutorService = true;
	protected boolean waitForTasksToCompleteOnShutdown = true;
	protected AsyncTokenFactory asyncTokenFactory = new DefaultAsyncTokenFactory();
	
	private String beanName;
	
	public void afterPropertiesSet() throws Exception {
		if(executorService == null && sendMailThreadPoolSize > 0) {
			executorService = Executors.newFixedThreadPool(sendMailThreadPoolSize,new CustomizableThreadFactory(getClass().getSimpleName()+"-"));
			log.info("create send mail executorService,sendMailThreadPoolSize:"+sendMailThreadPoolSize);
		}
		
		Assert.notNull(javaMailSender,"javaMailSender must be not null");
		Assert.notNull(executorService,"executorService must be not null");
		Assert.notNull(asyncTokenFactory,"asyncTokenFactory must be not null");
	}
	
	public void destroy() throws Exception {
		if(shutdownExecutorService) {
			shutdown();
		}
	}

	public void shutdown() {
		log.info("Shutting down ExecutorService" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
		
		if(waitForTasksToCompleteOnShutdown) 
			executorService.shutdown();
		else
			executorService.shutdownNow();
	}
	
	public void setBeanName(String name) {
		this.beanName = name;
	}
	
	public boolean isWaitForTasksToCompleteOnShutdown() {
		return waitForTasksToCompleteOnShutdown;
	}

	public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
		this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
	}

	public void setSendMailThreadPoolSize(int sendMailThreadPool) {
		this.sendMailThreadPoolSize = sendMailThreadPool;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}
	
	public AsyncTokenFactory getAsyncTokenFactory() {
		return asyncTokenFactory;
	}

	public void setAsyncTokenFactory(AsyncTokenFactory asyncTokenFactory) {
		this.asyncTokenFactory = asyncTokenFactory;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void setShutdownExecutorService(boolean shutdownExecutorService) {
		this.shutdownExecutorService = shutdownExecutorService;
	}

	public boolean isShutdownExecutorService() {
		return shutdownExecutorService;
	}
	
	public MimeMessage createMimeMessage() {
		return javaMailSender.createMimeMessage();
	}

	public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
		return javaMailSender.createMimeMessage(contentStream);
	}

	public AsyncToken send(final MimeMessage mimeMessage) throws MailException {
		return AsyncTokenUtils.execute(executorService,asyncTokenFactory, new Runnable() {
			public void run() {
				javaMailSender.send(mimeMessage);
			}
		});
	}

	public AsyncToken send(final MimeMessage[] mimeMessages) throws MailException {
		return AsyncTokenUtils.execute(executorService,asyncTokenFactory, new Runnable() {
			public void run() {
				javaMailSender.send(mimeMessages);
			}
		});
	}

	public AsyncToken send(final MimeMessagePreparator mimeMessagePreparator) throws MailException {	
		return AsyncTokenUtils.execute(executorService,asyncTokenFactory, new Runnable() {
			public void run() {
				javaMailSender.send(mimeMessagePreparator);
			}
		});
	}

	public AsyncToken send(final MimeMessagePreparator[] mimeMessagePreparators) throws MailException {		
		return AsyncTokenUtils.execute(executorService,asyncTokenFactory,new Runnable() {
			public void run() {
				javaMailSender.send(mimeMessagePreparators);
			}
		});
	}

	public AsyncToken send(final SimpleMailMessage simpleMessage) throws MailException {
		return AsyncTokenUtils.execute(executorService,asyncTokenFactory, new Runnable() {
			public void run() {
				javaMailSender.send(simpleMessage);
			}
		});
	}

	public AsyncToken send(final SimpleMailMessage[] simpleMessages) throws MailException {	
		return AsyncTokenUtils.execute(executorService,asyncTokenFactory, new Runnable() {
			public void run() {
				javaMailSender.send(simpleMessages);
			}
		});
	}

}
