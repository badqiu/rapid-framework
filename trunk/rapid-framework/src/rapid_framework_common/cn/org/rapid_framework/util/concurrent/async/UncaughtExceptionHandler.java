package cn.org.rapid_framework.util.concurrent.async;

public interface UncaughtExceptionHandler {
	
	void uncaughtException(IResponder responder,Throwable e);
	
}