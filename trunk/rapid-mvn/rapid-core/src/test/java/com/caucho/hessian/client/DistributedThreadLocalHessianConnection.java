package com.caucho.hessian.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


public class DistributedThreadLocalHessianConnection extends HessianURLConnection implements
		HessianConnection {
	
	public DistributedThreadLocalHessianConnection(URL url, URLConnection conn) {
		super(url, conn);
	}

	@Override
	protected void parseResponseHeaders(HttpURLConnection conn)
			throws IOException {
		super.parseResponseHeaders(conn);
		
		Map<String,List<String>> headers = conn.getHeaderFields();
		
	}

}