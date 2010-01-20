package cn.org.rapid_framework.web.session;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;

import cn.org.rapid_framework.web.mvc.Scope;

public class SessionRequestFilter extends OncePerRequestFilter implements Filter{

	@Override
	protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain chain)throws ServletException, IOException {
		Map sessionMap = Scope.Session.restore(request);
		try {
			Scope.Session session = new Scope.Session(sessionMap);
			Scope.Session.setCurrent(session);

			HttpSessionSidWrapper sessionIdWrapper = new HttpSessionSidWrapper(session.getId(),request.getSession());
			HttpSession finalSessionWrapper = new HttpSessionMapWrapper(sessionIdWrapper,sessionMap);

			HttpServletRequestSessionWrapper requestWrapper = new HttpServletRequestSessionWrapper(request,finalSessionWrapper);
			chain.doFilter(requestWrapper, response);
		}finally {
			Scope.Session.setCurrent(null);
			Scope.Session.save(response, sessionMap);
		}
	}

}
