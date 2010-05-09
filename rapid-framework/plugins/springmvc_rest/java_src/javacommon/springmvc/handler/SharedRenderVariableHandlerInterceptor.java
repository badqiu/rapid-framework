package javacommon.springmvc.handler;

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

/**
 * 拦截器,用于存放渲染视图时需要的的共享变量
 * @author badqiu
 *
 */
public class SharedRenderVariableHandlerInterceptor extends HandlerInterceptorAdapter implements InitializingBean{
	static Log log = LogFactory.getLog(SharedRenderVariableHandlerInterceptor.class);
	
	//系统启动并初始化一次的变量
	private Map globalRenderVariables = new HashMap();
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("请注意,在这里可以存放渲染视图时需要的的共享变量");
		
		modelAndView.addAllObjects(globalRenderVariables);
		
		modelAndView.addAllObjects(perRequest(request,response));
	}
	
	protected Map perRequest(HttpServletRequest request,HttpServletResponse response) {
		HashMap model = new HashMap();
		
		model.put("share_current_request_time", new Date());
		model.put("share_current_login_username", "badqiu");
		model.put("share_context_path", request.getContextPath());
		
		return model;
	}

	//用于初始化 sharedRenderVariables
	private void initSharedRenderVariables() {
		globalRenderVariables.put("global_system_start_time", new Date());
		
		//也可以存放一些共享的工具类,以便视图使用,如StringUtils
		
		// FormInputEnumUtils是工具类,可以将enum转换为Map类型的数据
		//sharedRenderVariables.put("userTypeEnum",FormInputEnumUtils.toMap(UserTypeEnum.values()));
		//sharedRenderVariables.put("areaEnum",FormInputEnumUtils.toMap(AreaEnum.values()));
	}
	
	//在系统启动时会执行
	public void afterPropertiesSet() throws Exception {
		initSharedRenderVariables();
	}
}
