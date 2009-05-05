package cn.org.rapid_framework.util.concurrent.async;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

/**
 * 该类主要用于得到异步方法的执行结果.
 * <pre>
 * 	AsyncToken token = sendEmail();
 * 	token.addResponder(new IResponder() {
 *		public void onFault(Throwable fault) {
 *			//handle fault 
 *		}
 *		public void onResult(Object result) {
 *			//handle result 
 *		}
 *	});
 * </pre>
 * 生成token请查看AsyncTokenTemplate
 * @see AsyncTokenTemplate
 * @author badqiu
 */
public class AsyncToken<T> {
	public static final String DEFAULT_TOKEN_GROUP = "default";
	
	//tokenGroup tokenName tokenDescription tokenId  用于可以增加描述信息
	private String tokenGroup = DEFAULT_TOKEN_GROUP;
	private String tokenName;
	private long tokenId;
	private String tokenDescription = null;
	
	private static UncaughtExceptionHandler defaultUncaughtExceptionHandler;
	
	private List<IResponder> _responders = new ArrayList(2);
	
	private UncaughtExceptionHandler uncaughtExceptionHandler;
	private T _result;
	private Exception _fault;
	private boolean _isFiredResult;

	
    private static long tokenNumSeqForTokenName;
    private static synchronized long nextTokenNum() {
    	return tokenNumSeqForTokenName++;
    }
    private static long tokenIdSequence;
    private static synchronized long nextTokenID() {
    	return ++tokenIdSequence;
    }
    
	public AsyncToken(){
		this(null);
	}
	
	public AsyncToken(UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
		init(DEFAULT_TOKEN_GROUP, "T-"+nextTokenNum(), nextTokenID());
	}
	
	public AsyncToken(String tokenGroup,String tokenName) {
		init(tokenGroup, tokenName, nextTokenID());
	}
	
	private void init(String tokenGroup,String tokenName,long tokenId) {
		setTokenGroup(tokenGroup);
		setTokenName(tokenName);
		this.tokenId = tokenId;
	}
	
	public String getTokenGroup() {
		return tokenGroup;
	}

	public void setTokenGroup(String tokenGroup) {
		if(tokenGroup == null) throw new IllegalArgumentException("'tokenGroup' must be not null");
		this.tokenGroup = tokenGroup;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		if(tokenName == null) throw new IllegalArgumentException("'tokenName' must be not null");
		this.tokenName = tokenName;
	}
	
	public String getTokenDescription() {
		return tokenDescription;
	}

	public void setTokenDescription(String tokenDescription) {
		this.tokenDescription = tokenDescription;
	}

	public long getTokenId() {
		return tokenId;
	}

	/**
	 * addResponder(responder,false);
	 * @param responder
	 */
	public void addResponder(final IResponder<T> responder) {
		addResponder(responder,false);
	}

	public void addResponder(final IResponder<T> responder,boolean invokeResponderInOtherThread) {
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
		}catch(Error e) {
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
	
	public void setComplete(T result) {
		if(_isFiredResult) throw new IllegalStateException("token already fired");
		this._result = result;
		fireResult2Responders();
	}
	
	public void setFault(Exception fault) {
		if(fault == null) throw new NullPointerException();
		if(_isFiredResult) throw new IllegalStateException("token already fired");
		this._fault = fault;
		fireResult2Responders();
	}
		
}
