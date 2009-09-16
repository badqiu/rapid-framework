package javacommon.mail;

import java.io.IOException;
import java.io.StringWriter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.Assert;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class BaseMailer implements InitializingBean{

	protected MailEngine mailEngine;
	protected SimpleMailMessage simpleMailMessageTemplate;
	protected Configuration freemarkerConfiguration;
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(mailEngine,"mailEngine must be not null");
		Assert.notNull(freemarkerConfiguration,"freemarkerConfiguration must be not null");
		Assert.notNull(simpleMailMessageTemplate,"simpleMailMessageTemplate must be not null");
	}
	
	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	public void setSimpleMailMessageTemplate(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessageTemplate = simpleMailMessage;
	}

	public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
		this.freemarkerConfiguration = freemarkerConfiguration;
	}

	protected SimpleMailMessage newSimpleMsgFromTemplate() {
		SimpleMailMessage msg = new SimpleMailMessage();
		simpleMailMessageTemplate.copyTo(msg);
		return msg;
	}

	protected String processWithTemplate(Object rootMap, String templateName) throws TemplateException, IOException {
		return processWithTemplate(rootMap, templateName, null);
	}

	private String processWithTemplate(Object model, String templateName,String encoding) throws TemplateException, IOException {
		StringWriter result = new StringWriter();
		Template template = null;
		if(encoding == null) {
			template = freemarkerConfiguration.getTemplate(templateName);
		}else {
			template = freemarkerConfiguration.getTemplate(templateName,encoding);
		}
		
		template.process(model, result);
		return result.toString();
	}



}
