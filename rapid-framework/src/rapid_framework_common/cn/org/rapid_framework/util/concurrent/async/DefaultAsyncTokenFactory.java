package cn.org.rapid_framework.util.concurrent.async;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

public class DefaultAsyncTokenFactory implements AsyncTokenFactory{
	
	private String tokenGroup = AsyncToken.DEFAULT_TOKEN_GROUP;
	private List<IResponder> responders;
	private UncaughtExceptionHandler uncaughtExceptionHandler;
	
	public String getTokenGroup() {
		return tokenGroup;
	}

	public void setTokenGroup(String tokenGroup) {
		this.tokenGroup = tokenGroup;
	}

	public List<IResponder> getResponders() {
		return responders;
	}

	public void setResponders(List<IResponder> responders) {
		Assert.notNull(responders,"responders must be not null");
		this.responders = responders;
	}
	
	public UncaughtExceptionHandler getUncaughtExceptionHandler() {
		return uncaughtExceptionHandler;
	}

	public void setUncaughtExceptionHandler(
			UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	public void addResponder(IResponder r) {
		if(responders == null) 
			responders = new ArrayList();
		this.responders.add(r);
	}
	
	@SuppressWarnings("unchecked")
	public AsyncToken newToken() {
		AsyncToken t = new AsyncToken();
		
		t.setTokenGroup(tokenGroup);
		t.setUncaughtExceptionHandler(uncaughtExceptionHandler);
		
		if(responders != null) {
			for(IResponder r : responders) {
				t.addResponder(r);
			}
		}
		
		return t;
	}

}
