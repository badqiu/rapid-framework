package javacommon.mail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import cn.org.rapid_framework.util.concurrent.async.IResponder;

public class MailerTemplate {

	protected List<IResponder> responders = new ArrayList();

	public void setResponders(List<IResponder> responders) {
		Assert.notNull(responders,"responders must be not null");
		this.responders = responders;
	}
	
	public AsyncToken send(MailerCallback callback) {
		AsyncToken token = callback.execute();
		if(token != null) {
			for(IResponder r : responders) {
				token.addResponder(r);
			}
		}
		return token;
	}

}
