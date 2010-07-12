package cn.org.rapid_framework.struts2.dispatcher;

import java.util.HashMap;

import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.mock.MockActionInvocation;

public class DirectResultTest {
	@Test
	public void test() throws Exception {
		DirectResult r = new DirectResult();
		MockActionInvocation invocation = new MockActionInvocation();
		invocation.setInvocationContext(new ActionContext(new HashMap()));
		
		r.doExecute("abc.jsp", invocation);
	}
	
}
