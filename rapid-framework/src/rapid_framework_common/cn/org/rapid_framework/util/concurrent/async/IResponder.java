package cn.org.rapid_framework.util.concurrent.async;

/**
 * @author badqiu
 */
public interface IResponder<T> {
	
	public void onResult(T result);
	
	public void onFault(Throwable fault);
	
}
