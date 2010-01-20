package cn.org.rapid_framework.web.session.wrapper;


import javax.servlet.http.HttpSession;


public class HttpSessionSidWrapper extends HttpSessionWrapper {
	private String sessionId;

	public HttpSessionSidWrapper(String sessionId,HttpSession session) {
		super(session);
		this.sessionId = sessionId;
	}

	@Override
	public String getId() {
		return sessionId;
	}

}
