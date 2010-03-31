package cn.org.rapid_framework.pipeline;

/**
 * @see Pipeline
 * @author badqiu
 */
public class PipeException extends RuntimeException {

	public PipeException() {
		super();
	}

	public PipeException(String message, Throwable cause) {
		super(message, cause);
	}

	public PipeException(String message) {
		super(message);
	}

	public PipeException(Throwable cause) {
		super(cause);
	}
	
}
