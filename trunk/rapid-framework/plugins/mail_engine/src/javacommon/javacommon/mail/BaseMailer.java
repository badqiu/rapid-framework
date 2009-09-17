package javacommon.mail;

import java.io.IOException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import cn.org.rapid_framework.freemarker.FreemarkerTemplateProcessor;
import cn.org.rapid_framework.mail.AsyncJavaMailSender;
import cn.org.rapid_framework.util.concurrent.async.AsyncTokenTemplate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * mailer基类,用于其它mailer继承.
 * 现本mailer与freemarker模板结合,用于生成邮件内容
 * 本类你也可以自由修改
 * 
 * Mailer使用示例:
 * 1. 包名: 放在mailer包内,如com.company.project.mailer
 * 2. 类名: 以Mailer结尾,如UserMailer
 * 3. 方法名: 
 * 			使用UserMailer.createXXXX()来创建邮件消息,如UserMailer.createNotifaction()
 * 			使用UserMailer.sendXXXX()来发送邮件,如UserMailer.sendNotifaction()
 * 4.必须继承之BaseMailer
 * 
 * @author badqiu
 *
 */
public class BaseMailer implements InitializingBean{

	protected AsyncJavaMailSender asyncJavaMailSender;
	protected SimpleMailMessage simpleMailMessageTemplate;
	protected AsyncTokenTemplate asyncTokenTemplate = new AsyncTokenTemplate();
	protected FreemarkerTemplateProcessor freemarkerTemplateProcessor;
	protected String subjectPrefix ; //邮件前缀,子类可以使用 
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(asyncJavaMailSender,"asyncJavaMailSender must be not null");
		Assert.notNull(asyncTokenTemplate,"asyncTokenTemplate must be not null");
		Assert.notNull(freemarkerTemplateProcessor,"freemarkerTemplateProcessor must be not null");
		subjectPrefix = subjectPrefix == null ? "" : subjectPrefix;
	}
	
	public void setAsyncJavaMailSender(AsyncJavaMailSender asyncJavaMailSender) {
		this.asyncJavaMailSender = asyncJavaMailSender;
	}
	
	public AsyncJavaMailSender getAsyncJavaMailSender() {
		return asyncJavaMailSender;
	}

	public void setSimpleMailMessageTemplate(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessageTemplate = simpleMailMessage;
	}
	
	public AsyncTokenTemplate getAsyncTokenTemplate() {
		return asyncTokenTemplate;
	}

	public void setAsyncTokenTemplate(AsyncTokenTemplate asyncTokenTemplate) {
		this.asyncTokenTemplate = asyncTokenTemplate;
	}

	public FreemarkerTemplateProcessor getFreemarkerTemplateProcessor() {
		return freemarkerTemplateProcessor;
	}

	public void setFreemarkerTemplateProcessor(FreemarkerTemplateProcessor freemarkerTemplateProcessor) {
		this.freemarkerTemplateProcessor = freemarkerTemplateProcessor;
	}

	public void setSubjectPrefix(String subjectPrefix) {
		this.subjectPrefix = subjectPrefix;
	}

	public String getSubjectPrefix() {
		return subjectPrefix;
	}
	
	/**
	 * 为subject 增加前缀
	 * @return
	 */
	public String wrapSubject(String subject) {
		String prefix = getSubjectPrefix() == null ? "" : getSubjectPrefix();
		return prefix + subject;
	}

	protected SimpleMailMessage newSimpleMsgFromTemplate(String subject) {
		SimpleMailMessage msg = new SimpleMailMessage();
		if(simpleMailMessageTemplate != null) {
			simpleMailMessageTemplate.copyTo(msg);
		}
		msg.setSubject(wrapSubject(subject));
		return msg;
	}

}
