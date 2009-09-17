package cn.org.rapid_framework.freemarker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import cn.org.rapid_framework.util.concurrent.async.IResponder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * freemarker的template处理类，用于将check exception转换为uncheck exception
 * @author badqiu
 */
public class FreemarkerTemplateProcessor implements InitializingBean{
	protected Configuration freemarkerConfiguration;
	protected String encoding = null;
	protected Locale locale = null;
	
	public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
		this.freemarkerConfiguration = freemarkerConfiguration;
	}
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(freemarkerConfiguration,"freemarkerConfiguration must be not null");
		locale = freemarkerConfiguration.getLocale();
	}
	
	public String processTemplate(String templateName, Object model) {
		Template template = getTemplate(templateName);
		return processTemplateIntoString(template, model);
	}

	public static String processTemplateIntoString(Template template,Object model) {
		try {
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		} catch (IOException e) {
			throw new FreemarkerTemplateException("process template occer IOException,templateName:"+template.getName()+" cause:"+e,e);
		} catch (TemplateException e) {
			throw new FreemarkerTemplateException("process template occer TemplateException,templateName:"+template.getName()+" cause:"+e,e);
		}
	}

	private Template getTemplate(String templateName) {
		try {
			if(encoding == null) {
				return freemarkerConfiguration.getTemplate(templateName,locale);
			}else {
				return freemarkerConfiguration.getTemplate(templateName,locale,encoding);
			}
		} catch (IOException e) {
			throw new FreemarkerTemplateException("load template error,templateName:"+templateName+" cause:"+e,e);
		}
	}

}
