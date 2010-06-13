package javacommon.struts2.interceptor;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import cn.org.rapid_framework.web.httpinclude.HttpInclude;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.util.ValueStack;
/**
 * 提供视图渲染的共享变量拦截器
 * @author badqiu
 *
 */
public class SharedRenderVariableInterceptor implements Interceptor {
	static Log log = LogFactory.getLog(SharedRenderVariableInterceptor.class);
	
	//系统启动并初始化一次的变量
	Map<String,Object> globalRenderVariables = new HashMap();
	
	public String intercept(ActionInvocation invocation) throws Exception {
		before(invocation);
		String result = invocation.invoke();
		return result;
	}

	private void before(ActionInvocation invocation) {
		log.info("请注意,在这里可以存放渲染视图时需要的的共享变量");
		ValueStack vs = invocation.getInvocationContext().getValueStack();
		for(String key : globalRenderVariables.keySet()) {
			vs.set(key, globalRenderVariables.get(key));
		}
		
		preRequest(vs,invocation);
	}
	
	private void preRequest(ValueStack vs,ActionInvocation invocation) {
		vs.set("share_current_request_time", new Date());
		vs.set("share_current_login_username", "badqiu");
		
		//为freemarker,velocity提供<jsp:include page="/some/page.jsp"/>功能,使用示例: ${httpInclude.include("/servlet/header.do")};
		vs.set("httpInclude",new HttpInclude(ServletActionContext.getRequest(),ServletActionContext.getResponse()));
	}

	//注意,如果变量是global,请尽量增加global前缀
	private void initSharedRenderVariables() {
		globalRenderVariables.put("global_system_start_time", new Date());
		globalRenderVariables.put("url_prefix", "http://www.rapid-framework.org.cn");
		globalRenderVariables.put("media_url_prefix", "/images");
	}
	
	public void destroy() {
	}

	public void init() {
		initSharedRenderVariables();
	}
	
}
