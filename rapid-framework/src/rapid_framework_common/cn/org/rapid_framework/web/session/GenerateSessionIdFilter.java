package cn.org.rapid_framework.web.session;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.org.rapid_framework.web.session.wrapper.HttpSessionSidWrapper;
import cn.org.rapid_framework.web.util.CookieUtils;
import cn.org.rapid_framework.web.util.FilterConfigUtils;

public class GenerateSessionIdFilter  extends OncePerRequestFilter implements Filter{
	private String sessionIdCookieName = "RAPID_SESSION_ID";

	private String cookieDomain = "";

	private String cookiePath = "/";

	private Integer sessionTimeout = null;

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		sessionTimeout = FilterConfigUtils.getIntegerParameter(getFilterConfig(), "sessionTimeout", null);
		cookieDomain = FilterConfigUtils.getParameter(getFilterConfig(), "cookieDomain", cookieDomain);
		cookiePath = FilterConfigUtils.getParameter(getFilterConfig(), "cookiePath", cookiePath);
		sessionIdCookieName = FilterConfigUtils.getParameter(getFilterConfig(), "sessionIdCookieName", sessionIdCookieName);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain chain)throws ServletException, IOException {
		Map<String,Cookie> cookieMap = CookieUtils.toMap(request.getCookies());
		Cookie sessionIdCookie = cookieMap.get(sessionIdCookieName);
		if(sessionIdCookie == null || StringUtils.isEmpty(sessionIdCookie.getValue())) {
			sessionIdCookie = generateCookie(response);
		}

		HttpSessionSidWrapper sessionWrapper = new HttpSessionSidWrapper(sessionIdCookie.getValue(),request.getSession());
		chain.doFilter(new HttpServletRequestSessionWrapper(request,sessionWrapper), response);

	}

	private Cookie generateCookie(HttpServletResponse response) {
		Cookie sessionIdCookie;
		String sid = java.util.UUID.randomUUID().toString();
		sessionIdCookie = new Cookie(sessionIdCookieName,sid);
		sessionIdCookie.setDomain(cookieDomain);
		sessionIdCookie.setPath(cookiePath);
		sessionIdCookie.setMaxAge(sessionTimeout == null ? -1 : sessionTimeout * 60 * 1000);
		response.addCookie(sessionIdCookie);
		return sessionIdCookie;
	}
}
