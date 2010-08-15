package net.sf.log4jdbc;

import java.sql.Connection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class DataSourceSpyInterceptor implements MethodInterceptor {

	private boolean enabled = Boolean.parseBoolean(System.getProperty("log4jdbc.enabled","false"));
	private RdbmsSpecifics rdbmsSpecifics = null;
	
    private RdbmsSpecifics getRdbmsSpecifics(Connection conn) {
        if(rdbmsSpecifics == null) {
            try {
                rdbmsSpecifics = DriverSpy.getRdbmsSpecifics(conn);
            }catch(Exception e) {
                System.err.println("error on getRdbmsSpecifics(Connection conn),caused:"+e.toString());
            }
        }
        return rdbmsSpecifics;
    }
    
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = invocation.proceed();
		if(result instanceof Connection) {
			Connection conn = (Connection)result;
			if(enabled) {
				return new ConnectionSpy(conn,getRdbmsSpecifics(conn));
			}
		}
		return result;
	}

}
