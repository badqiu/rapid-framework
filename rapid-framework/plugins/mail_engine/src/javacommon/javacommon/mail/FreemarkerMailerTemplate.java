package javacommon.mail;

import java.io.IOException;
import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import cn.org.rapid_framework.util.concurrent.async.IResponder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * 邮件发送的template类
 * @author badqiu
 */
public class FreemarkerMailerTemplate extends MailerTemplate {
	protected Configuration freemarkerConfiguration;
	protected String templateEncoding = null;
	
	public void setResponders(List<IResponder> responders) {
		this.responders = responders;
	}

	public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
		this.freemarkerConfiguration = freemarkerConfiguration;
	}
	
	public void setTemplateEncoding(String templateEncoding) {
		this.templateEncoding = templateEncoding;
	}

	public AsyncToken send(String templateName,Object model,SimpleMailMessage msg,MailerCallback callback) {
		processTemplateIntoMsgText(templateName, model, msg);
		return send(callback);
	}

	public void processTemplateIntoMsgText(String templateName, Object model,SimpleMailMessage msg) {
		Template template = getTemplate(templateName);
		try {
			String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			msg.setText(text);
		} catch (IOException e) {
			throw new FreemarkerTemplateException("process template occer IOException,templateName:"+templateName+" cause:"+e,e);
		} catch (TemplateException e) {
			throw new FreemarkerTemplateException("process template occer TemplateException,templateName:"+templateName+" cause:"+e,e);
		}
	}

	private Template getTemplate(String templateName) {
		try {
			if(templateEncoding != null) {
				return freemarkerConfiguration.getTemplate(templateName,templateEncoding);
			}
			return freemarkerConfiguration.getTemplate(templateName);
		} catch (IOException e) {
			throw new FreemarkerTemplateException("load template error,templateName:"+templateName+" cause:"+e,e);
		}
	}
	
}
