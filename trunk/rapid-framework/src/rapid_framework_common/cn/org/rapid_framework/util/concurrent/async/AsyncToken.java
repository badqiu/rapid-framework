package cn.org.rapid_framework.util.concurrent.async;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

/**
 * @author badqiu
 */
public class AsyncToken {
	
	private static UncaughtExceptionHandler defaultUncaughtExceptionHandler;
	
	private List<IResponder> _responders = new ArrayList(2);
	
	private UncaughtExceptionHandler uncaughtExceptionHandler;
	private Object _result;
	private Throwable _fault;
	private boolean _isFiredResult;
	
	public AsyncToken(){}
	
	public AsyncToken(UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	public void addResponder(final IResponder responder) {
		addResponder(responder,true);
	}
	
	public void addResponder(final IResponder responder,boolean invokeResponderInOtherThread) {
		_responders.add(responder);
		
		if(_isFiredResult) {
			if(invokeResponderInOtherThread) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fireResult2Responder(responder);
					}
				});
			}else {
				fireResult2Responder(responder);
			}
		}
	}
	
	public List<IResponder> getResponders() {
		return _responders;
	}
	
	public boolean hasResponder() {
		return _responders != null && _responders.size() > 0;
	}
	
	public static UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
		return defaultUncaughtExceptionHandler;
	}

	public static void setDefaultUncaughtExceptionHandler(
			UncaughtExceptionHandler defaultUncaughtExceptionHandler) {
		AsyncToken.defaultUncaughtExceptionHandler = defaultUncaughtExceptionHandler;
	}

	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return uncaughtExceptionHandler == null ? defaultUncaughtExceptionHandler : uncaughtExceptionHandler;
	}
	
	public void setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}	
	
	private void fireResult2Responder(IResponder responder) {
		try {
			if(_fault != null) {
				responder.onFault(_fault);
			}else {
				responder.onResult(_result);
			}
		}catch(RuntimeException e) {
			if(getUncaughtExceptionHandler() != null) {
				getUncaughtExceptionHandler().uncaughtException(responder, e);
			}else {
				throw e;
			}
		}
	}
	
	private void fireResult2Responders() {
		_isFiredResult = true;
		for(IResponder r : _responders) {
			fireResult2Responder(r);
		}
	}

	public void setComplete(){
		setComplete(null);
	}
	
	public void setComplete(Object result) {
		if(_isFiredResult) throw new IllegalArgumentException("token already fired");
		this._result = result;
		fireResult2Responders();
	}
	
	public void setFault(Throwable fault) {
		if(fault == null) throw new NullPointerException();
		if(_isFiredResult) throw new IllegalArgumentException("token already fired");
		this._fault = fault;
		fireResult2Responders();
	}
	
	public static interface UncaughtExceptionHandler {
		void uncaughtException(IResponder responder,Throwable e);
	}
	
}
