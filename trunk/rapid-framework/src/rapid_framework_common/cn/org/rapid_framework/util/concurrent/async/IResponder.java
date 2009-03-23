package cn.org.rapid_framework.util.concurrent.async;

/**
 * @author badqiu
 */
public interface IResponder {
	
	public void onResult(Object result);
	
	public void onFault(Throwable fault);
	
}
