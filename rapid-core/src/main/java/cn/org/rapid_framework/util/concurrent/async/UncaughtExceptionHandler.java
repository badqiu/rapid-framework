package cn.org.rapid_framework.util.concurrent.async;

/**
 * 用于捕获AsyncToken在通知IResponder结果时,IResponder的抛出的异常
 * @author badqiu
 *
 */
@SuppressWarnings("all")
public interface UncaughtExceptionHandler {
	
	void uncaughtException(IResponder responder,Throwable e);
	
}