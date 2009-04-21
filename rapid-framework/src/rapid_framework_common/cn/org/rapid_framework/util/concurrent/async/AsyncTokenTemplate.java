package cn.org.rapid_framework.util.concurrent.async;

import java.util.ArrayList;
import java.util.List;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken.UncaughtExceptionHandler;

/**
 * AsyncToken模板方法,用于生成token,使用方法如下:
 * <pre>
 * 	public AsyncToken sendEmail() {
 *		AsyncToken token = new AsyncTokenTemplate().execute(new AsyncTokenCallback() {
 *			public Object execute() throws Throwable {
 *				executeSendEmailMsg();
 *				return result;
 *			}
 *		});
 *		return token;
 *	}
 * </pre>
 * @author badqiu
 *
 */
public class AsyncTokenTemplate {
	private List<IResponder> responders = new ArrayList(1);
	private UncaughtExceptionHandler uncaughtExceptionHandler;
	
	private String tokenGroup = AsyncToken.DEFAULT_TOKEN_GROUP;
	
	public AsyncTokenTemplate() {
	}
	
	public AsyncTokenTemplate(UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}
	
	public AsyncTokenTemplate(String tokenGroup,
			UncaughtExceptionHandler uncaughtExceptionHandler) {
		super();
		setTokenGroup(tokenGroup);
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return uncaughtExceptionHandler;
	}

	public void setUncaughtExceptionHandler(
			UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	public AsyncTokenTemplate(IResponder responder) {
		super();
		this.responders.add(responder);
	}

	public List<IResponder> getResponders() {
		return responders;
	}

	public void setResponders(List<IResponder> responders) {
		this.responders = responders;
	}
	
	public String getTokenGroup() {
		return tokenGroup;
	}

	public void setTokenGroup(String tokenGroup) {
		if(tokenGroup == null) throw new IllegalArgumentException("'tokenGroup' must be not null");
		this.tokenGroup = tokenGroup;
	}

	public AsyncToken execute(AsyncTokenCallback callback) {
		AsyncToken token = newAsyncToken();
		try {
			Object result = callback.execute();
			token.setComplete(result);
		} catch (Throwable e) {
			token.setFault(e);
		}
		return token;
	}

	protected AsyncToken newAsyncToken() {
		AsyncToken token = new AsyncToken();
		token.setTokenGroup(tokenGroup);
		
		for(IResponder r : responders) {
			token.addResponder(r);
		}
		
		token.setUncaughtExceptionHandler(uncaughtExceptionHandler);
		
		setTokenProperties(token);
		return token;
	}
	
	/**
	 * for override to set token properties
	 * @param token
	 */
	protected void setTokenProperties(AsyncToken token) {
	}

	public static interface AsyncTokenCallback {
		
		Object execute() throws Throwable;
		
	}
	
}
