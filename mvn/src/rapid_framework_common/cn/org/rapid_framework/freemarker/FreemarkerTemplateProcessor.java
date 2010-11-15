package cn.org.rapid_framework.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import cn.org.rapid_framework.freemarker.directive.DirectiveUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * freemarker的template处理类，用于将check exception转换为uncheck exception
 * 并提供相关工具类方法
 * @author badqiu
 */
public class FreemarkerTemplateProcessor implements InitializingBean{
	static Log log = LogFactory.getLog(FreemarkerTemplateProcessor.class);
	private Configuration configuration;
	private boolean exposeRapidMacros = true;
	
	public FreemarkerTemplateProcessor() {}
	
	public FreemarkerTemplateProcessor(Configuration configuration) {
		setConfiguration(configuration);
	}

	public void setConfiguration(Configuration freemarkerConfiguration) {
		this.configuration = freemarkerConfiguration;
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}
	
	public void setExposeRapidMacros(boolean exposeRapidMacros) {
		this.exposeRapidMacros = exposeRapidMacros;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(configuration,"configuration property must be not null");
		if(exposeRapidMacros) {
			if(log.isInfoEnabled())
				log.info("expose rapid macros: <@block> <@extends> <@override> for freemarker");
			DirectiveUtils.exposeRapidMacros(configuration);
		}
	}
	
	public String processTemplate(String templateName, Object model) throws FreemarkerTemplateException{
		Template template = getTemplate(configuration,templateName);
		return processTemplateIntoString(template, model);
	}
	
	public void processTemplate(String templateName, Object model,Writer out) throws FreemarkerTemplateException{
		Template template = getTemplate(configuration,templateName);
		processTemplate(template, model, out);
	}
	
	public String processTemplate(String templateName, Object model,String encoding) throws FreemarkerTemplateException {
		Template template = getTemplate(configuration,templateName,encoding);
		return processTemplateIntoString(template, model);
	}
	
	public String processTemplate(String templateName, Object model,Locale locale,String encoding) throws FreemarkerTemplateException {
		Template template = getTemplate(configuration,templateName,locale,encoding);
		return processTemplateIntoString(template, model);
	}

	public static String processTemplateIntoString(Template template,Object model) throws FreemarkerTemplateException {
		StringWriter out = new StringWriter(512);
		processTemplate(template, model, out);
		return out.toString();
	}
	
	public static void processTemplate(Template template,Object model,Writer out) throws FreemarkerTemplateException {
		try {
			template.process(model, out);
		} catch (IOException e) {
			throw new FreemarkerTemplateException("process template occer IOException,templateName:"+template.getName(),e);
		} catch (TemplateException e) {
			throw new FreemarkerTemplateException("process template occer TemplateException,templateName:"+template.getName(),e);
		}
	}

	public static Template getTemplate(Configuration conf,String templateName) throws FreemarkerTemplateException {
		try {
			return conf.getTemplate(templateName);
		} catch (IOException e) {
			throw new FreemarkerTemplateException("load template error,templateName:"+templateName,e);
		}
	}
	
	public static Template getTemplate(Configuration conf,String templateName,String encoding) throws FreemarkerTemplateException {
		try {
			return conf.getTemplate(templateName,encoding);
		} catch (IOException e) {
			throw new FreemarkerTemplateException("load template error,templateName:"+templateName,e);
		}
	}
	
	public static Template getTemplate(Configuration conf,String templateName,Locale locale,String encoding) throws FreemarkerTemplateException {
		try {
			return conf.getTemplate(templateName,locale,encoding);
		} catch (IOException e) {
			throw new FreemarkerTemplateException("load template error,templateName:"+templateName,e);
		}
	}

}
