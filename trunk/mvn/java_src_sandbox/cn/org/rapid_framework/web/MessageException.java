package cn.org.rapid_framework.web;

public class MessageException extends RuntimeException implements Message {

	private static final long serialVersionUID = -7111246458366813892L;

	public MessageException() {
		super();
	}

	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageException(String message) {
		super(message);
	}

	public MessageException(Throwable cause) {
		super(cause);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
}
