package cn.org.rapid_framework.web.scope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.org.rapid_framework.web.scope.Scope;
import cn.org.rapid_framework.web.scope.Scope.RenderArgs;

public class ScopeRenderArgsInterceptor extends HandlerInterceptorAdapter{

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
		RenderArgs current = Scope.RenderArgs.current();
		if(current != null) {
			modelAndView.addAllObjects(current.data);
		}
	}
	
}
