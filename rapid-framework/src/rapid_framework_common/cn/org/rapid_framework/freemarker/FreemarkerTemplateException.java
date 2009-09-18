package cn.org.rapid_framework.freemarker;

/**
 * FreemarkerException等价的异常类，不过继承之RuntimeException
 * @author badqiu
 *
 */
public class FreemarkerTemplateException extends RuntimeException {

	public FreemarkerTemplateException() {
		super();
	}

	public FreemarkerTemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public FreemarkerTemplateException(String message) {
		super(message);
	}

	public FreemarkerTemplateException(Throwable cause) {
		super(cause);
	}
	
}
