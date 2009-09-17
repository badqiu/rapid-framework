package javacommon.mail;

import java.util.ArrayList;
import java.util.List;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import cn.org.rapid_framework.util.concurrent.async.IResponder;

public class MailerTemplate {

	protected List<IResponder> responders = new ArrayList();

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
