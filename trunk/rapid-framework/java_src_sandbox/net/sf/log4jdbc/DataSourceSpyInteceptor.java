package net.sf.log4jdbc;

import java.sql.Connection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DataSourceSpyInteceptor implements MethodInterceptor {

	private boolean enabled = Boolean.parseBoolean(System.getProperty("log4jdbc.enabled","false"));

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = invocation.proceed();
		if(result instanceof Connection) {
			Connection conn = (Connection)result;
			if(enabled) {
				return new ConnectionSpy(conn);
			}
		}
		return result;
	}

}
