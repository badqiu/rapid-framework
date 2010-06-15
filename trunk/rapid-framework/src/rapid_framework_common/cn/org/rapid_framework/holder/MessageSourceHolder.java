package cn.org.rapid_framework.holder;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
/**
 * 用于持有spring的messageSource,一个系统只能有一个MessageSourceHolder 
 * 
 * 
 * <br />
 * <pre>
 * 使用方法:
 * &lt;bean class="cn.org.rapid_framework.holder.MessageSourceHolder"/>
 * 
 * 在java代码中则可以如此使用: 
 * BlogDao dao = (BlogDao)MessageSourceHolder.getBean("blogDao");
 * </pre>
 * @author badqiu
 */
public class MessageSourceHolder implements MessageSourceAware{
	
	private static Log log = LogFactory.getLog(MessageSourceHolder.class);
	private static MessageSource messageSource;
	
	public void setMessageSource(MessageSource message) throws BeansException {
		if(this.messageSource != null) {
			throw new IllegalStateException("MessageSourceHolder already holded 'messageSource'.");
		}
		this.messageSource = message;
		log.info("holded messageSource,displayName:"+messageSource);
	}
	
	public static MessageSource getMessageSource() {
		if(messageSource == null)
			throw new IllegalStateException("'messageSource' property is null,MessageSourceHolder not yet init.");
		return messageSource;
	}

	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		return getMessageSource().getMessage(resolvable, locale);
	}

	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		return getMessageSource().getMessage(code, args, locale);
	}

	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		return getMessageSource().getMessage(code, args, defaultMessage, locale);
	}

	public static void cleanHolder() {
		messageSource = null;
	}
}
