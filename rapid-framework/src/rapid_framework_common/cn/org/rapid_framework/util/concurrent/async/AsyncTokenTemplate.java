package cn.org.rapid_framework.util.concurrent.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


import org.springframework.util.Assert;


public class AsyncTokenTemplate {

	protected List<IResponder> responders = new ArrayList();

	public void setResponders(List<IResponder> responders) {
		Assert.notNull(responders,"responders must be not null");
		this.responders = responders;
	}
	
	public AsyncToken execute(AsyncTokenCallback callback) {
		AsyncToken token = callback.execute();
		if(token != null) {
			for(IResponder r : responders) {
				token.addResponder(r);
			}
		}
		return token;
	}
	
	/**
	 * set AsyncTokenUtils.execute();
	 */
	@Deprecated
	public static void execute(AsyncToken token,Callable task) {
		AsyncTokenUtils.execute(token, task);
	}
	
}
