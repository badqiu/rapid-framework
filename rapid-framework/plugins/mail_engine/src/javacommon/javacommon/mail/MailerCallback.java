package javacommon.mail;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public interface MailerCallback {
	
	public AsyncToken execute() ;
	
}
