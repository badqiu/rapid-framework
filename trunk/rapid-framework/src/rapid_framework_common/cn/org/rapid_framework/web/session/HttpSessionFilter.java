package cn.org.rapid_framework.web.session;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.org.rapid_framework.web.session.store.SessionStore;
import cn.org.rapid_framework.web.session.wrapper.HttpServletRequestSessionWrapper;
import cn.org.rapid_framework.web.session.wrapper.HttpSessionSessionStoreWrapper;
import cn.org.rapid_framework.web.util.CookieUtils;
import cn.org.rapid_framework.web.util.FilterConfigUtils;

public class HttpSessionFilter  extends OncePerRequestFilter implements Filter{
	private String sessionIdCookieName = "RAPID_SESSION_ID";

	private String cookieDomain = "";

	private String cookiePath = "/";

	SessionStore sessionStore;
	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		cookieDomain = FilterConfigUtils.getParameter(getFilterConfig(), "cookieDomain", cookieDomain);
		cookiePath = FilterConfigUtils.getParameter(getFilterConfig(), "cookiePath", cookiePath);
		sessionIdCookieName = FilterConfigUtils.getParameter(getFilterConfig(), "sessionIdCookieName", sessionIdCookieName);

		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain chain)throws ServletException, IOException {
		Cookie sessionIdCookie = getOrGenerateSessionId(request, response);
		String sessionId = sessionIdCookie.getValue();

		HttpSession rawSession = request.getSession();
		Map sessionData = sessionStore.getSession(request, sessionId,rawSession.getMaxInactiveInterval());
		try {
			HttpSession sessionWrapper = new HttpSessionSessionStoreWrapper(rawSession,
					request,response,
					sessionStore,sessionId,sessionData);
			
			chain.doFilter(new HttpServletRequestSessionWrapper(request,sessionWrapper), response);
		}finally {
			sessionStore.saveSession(response, sessionId, sessionData,rawSession.getMaxInactiveInterval());
			sessionIdCookie.setMaxAge(request.getSession().getMaxInactiveInterval() * 60 * 60 * 1000);
			response.addCookie(sessionIdCookie);
		}
	}

	private Cookie getOrGenerateSessionId(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Cookie> cookieMap = CookieUtils.toMap(request.getCookies());
		Cookie sessionIdCookie = cookieMap.get(sessionIdCookieName);
		if(sessionIdCookie == null || StringUtils.isEmpty(sessionIdCookie.getValue())) {
			sessionIdCookie = generateCookie(request,response);
		}
		return sessionIdCookie;
	}

	private Cookie generateCookie(HttpServletRequest request,HttpServletResponse response) {
		Cookie sessionIdCookie;
		String sid = java.util.UUID.randomUUID().toString();
		sessionIdCookie = new Cookie(sessionIdCookieName,sid);
		sessionIdCookie.setDomain(cookieDomain);
		sessionIdCookie.setPath(cookiePath);
		sessionIdCookie.setMaxAge(request.getSession().getMaxInactiveInterval() * 60 * 60 * 1000);
		response.addCookie(sessionIdCookie);
		return sessionIdCookie;
	}
}
