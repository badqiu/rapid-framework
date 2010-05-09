package javacommon.base.struts2.Interceptor;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.util.ValueStack;

public class SharedRenderVariableInterceptor implements Interceptor {
	static Log log = LogFactory.getLog(SharedRenderVariableInterceptor.class);
	
	//系统启动并初始化一次的变量
	Map<String,Object> globalRenderVariables = new HashMap();
	
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = invocation.invoke();
		after(invocation);
		return result;
	}

	private void after(ActionInvocation invocation) {
		log.info("请注意,在这里可以存放渲染视图时需要的的共享变量");
		ValueStack vs = invocation.getInvocationContext().getValueStack();
		for(String key : globalRenderVariables.keySet()) {
			vs.set(key, globalRenderVariables.get(key));
		}
		
		preRequest(vs);
	}

	private void preRequest(ValueStack vs) {
		vs.set("share_current_request_time", new Date());
		vs.set("share_current_login_username", "badqiu");
	}

	private void initSharedRenderVariables() {
		globalRenderVariables.put("global_system_start_time", new Date());
	}
	
	public void destroy() {
	}

	public void init() {
		initSharedRenderVariables();
	}
	
}
