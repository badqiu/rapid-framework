package cn.org.rapid_framework.cxf;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
/**
 * 开发调试使用的 Jetty Server
 * @author badqiu
 *
 */
public class CXFJettyServer {
	
	public static void main(String[] args) throws Exception {
		start();
	}
	
	public static void stop() throws Exception {
		server.stop();
	}
	
	static Server server = buildNormalServer(8080, "/");
	public static void start() throws Exception {
		server.start();
	}	
	/**
	 * 创建用于正常运行调试的Jetty Server, 以src/main/webapp为Web应用目录.
	 */
	public static Server buildNormalServer(int port, String contextPath) {
		Server server = new Server(port);
		WebAppContext webContext = new WebAppContext("src/test/resources/cxf_service", contextPath);
		webContext.setClassLoader(Thread.currentThread().getContextClassLoader());
		server.setHandler(webContext);
		server.setStopAtShutdown(true);
		return server;
	}


}
