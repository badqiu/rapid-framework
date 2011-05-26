package cn.org.rapid_framework.hessian.HessianTest;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface Hello {
	@WebMethod
	public void say();
	
	@WebMethod
	public void exception();
}