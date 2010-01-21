package cn.org.rapid_framework.web.scope;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;


public class ScopeFilter extends OncePerRequestFilter implements Filter{

	@Override
	protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain chain)throws ServletException, IOException {
		try {
			Scope.Flash.setCurrent(Scope.Flash.restore(request));
			Scope.RenderArgs.setCurrent(new Scope.RenderArgs());
			chain.doFilter(request, response);
		}finally {
			Scope.Flash.current().save(response);
			Scope.Flash.setCurrent(null);
			Scope.RenderArgs.setCurrent(null);
		}
	}

}
