package cn.org.rapid_framework.freemarker;

/**
 * FreemarkerException等价的异常类，不过继承之RuntimeException
 * @author badqiu
 *
 */
public class FreemarkerTemplateException extends RuntimeException {

	private static final long serialVersionUID = -3001339513837419069L;

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
