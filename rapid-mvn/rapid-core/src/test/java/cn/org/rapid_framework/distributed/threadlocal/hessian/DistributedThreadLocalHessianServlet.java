package cn.org.rapid_framework.distributed.threadlocal.hessian;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import cn.org.rapid_framework.distributed.threadlocal.DistributedThreadLocal;

import com.caucho.hessian.server.HessianServlet;

public class DistributedThreadLocalHessianServlet extends HessianServlet{

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws IOException, ServletException {
		super.service(request, response);
	}
}
