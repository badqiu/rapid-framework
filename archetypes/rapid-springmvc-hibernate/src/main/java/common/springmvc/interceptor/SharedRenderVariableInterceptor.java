package common.springmvc.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.org.rapid_framework.web.httpinclude.HttpInclude;
import cn.org.rapid_framework.web.scope.Flash;

/**
 * 拦截器,用于存放渲染视图时需要的的共享变量
 * @author badqiu
 *
 */
public class SharedRenderVariableInterceptor extends HandlerInterceptorAdapter implements InitializingBean {
	static Log log = LogFactory.getLog(SharedRenderVariableInterceptor.class);

	//系统启动并初始化一次的变量
	private Map<String, Object> globalRenderVariables = new HashMap<String, Object>();

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("请注意,在这里可以存放渲染视图时需要的的共享变量");

		if (modelAndView == null) {
			return;
		}

		String viewName = modelAndView.getViewName();
		if (viewName != null && !viewName.startsWith("redirect:")) {
			modelAndView.addAllObjects(globalRenderVariables);
			modelAndView.addAllObjects(perRequest(request, response));
		}
	}

	protected Map<String, Object> perRequest(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> model = new HashMap<String, Object>();

		model.put("share_current_request_time", new Date());
		model.put("share_current_login_username", "badqiu");
		model.put("ctx", request.getContextPath());
		model.put("flash", Flash.current().getData());

		//为freemarker,velocity提供<jsp:include page="/some/page.jsp"/>功能,使用${httpInclude.include("/servlet/header.do")};
		model.put("httpInclude", new HttpInclude(request, response));

		return model;
	}

	//用于初始化 sharedRenderVariables, 全局共享变量请尽量用global前缀
	private void initSharedRenderVariables() {
		globalRenderVariables.put("global_system_start_time", new Date());
		globalRenderVariables.put("url_prefix", "http://www.rapid-framework.org.cn");
		globalRenderVariables.put("img_url_prefix", "http://img.rapid-framework.org.cn");
		globalRenderVariables.put("media_url_prefix", "http://media.rapid-framework.org.cn");
		globalRenderVariables.put("static_url_prefix", "http://static.rapid-framework.org.cn");

		//也可以存放一些共享的工具类,以便视图使用,如StringUtils

	}

	//在系统启动时会执行
	public void afterPropertiesSet() throws Exception {
		initSharedRenderVariables();
	}
}
