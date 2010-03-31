package cn.org.rapid_framework.web.filter;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import junit.framework.TestCase;

import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class RestUrlRewriteFilterTest extends TestCase {
	
	RestUrlRewriteFilter filter = new RestUrlRewriteFilter();
	MockFilterChain chain = new MockFilterChain();
	RestMockHttpServletRequest request = new RestMockHttpServletRequest();
	MockHttpServletResponse response = new MockHttpServletResponse();
	MockFilterConfig config = new MockFilterConfig();
	
	public void setUp() throws ServletException {
		config.addInitParameter("debug", "true");
		filter.init(config);
	}
	
	public void testPrifix() throws ServletException, IOException {
		config.addInitParameter("prefix", "/foo/");
		filter.init(config);
		
		request.setRequestURI("/user.css");
		filter.doFilter(request, response, new MockFilterChain());
		assertEquals("/foo//user.css",request.rewritePath);
	}
	
	public void testRewriteURL() throws ServletException, IOException {
		request.setRequestURI("/user.css");
		filter.doFilter(request, response, new MockFilterChain());
		assertEquals("/static/user.css",request.rewritePath);
		
		config.addInitParameter("excludeExtentions", "foo");
		filter.init(config);
		assertEquals("/static/user.css",request.rewritePath);
	}

	public void testExcludeExtensions() throws ServletException, IOException {
		request.setRequestURI("/user.jsp");
		filter.doFilter(request, response, new MockFilterChain());
		assertEquals(null,request.rewritePath);
		
		request.setRequestURI("/user.jspx");
		filter.doFilter(request, response, new MockFilterChain());
		assertEquals(null,request.rewritePath);
		
		request.setRequestURI("/user.do");
		filter.doFilter(request, response, new MockFilterChain());
		assertEquals(null,request.rewritePath);
		
		request.setRequestURI("/user");
		filter.doFilter(request, response, new MockFilterChain());
		assertEquals(null,request.rewritePath);
		
		config.addInitParameter("excludeExtentions", "foo");
		filter.init(config);
		request.setRequestURI("/user.foo");
		filter.doFilter(request, response, new MockFilterChain());
		assertEquals(null,request.rewritePath);
	}
	
	public void testExcludePrefixs() throws ServletException, IOException {
		config.addInitParameter("excludePrefixes", "/scripts,/images");
		filter.init(config);
		request.setRequestURI("/scripts/foo.js");
		filter.doFilter(request, response, new MockFilterChain());
		assertEquals(null,request.rewritePath);
		
		request.setRequestURI("/images/foo.gif");
		filter.doFilter(request, response, new MockFilterChain());
		assertEquals(null,request.rewritePath);
		
		request.setRequestURI("/foo.gif");
		filter.doFilter(request, response, new MockFilterChain());
		assertEquals("/static/foo.gif",request.rewritePath);
	}
	
	class RestMockHttpServletRequest extends MockHttpServletRequest{
		public String rewritePath;
		public RequestDispatcher getRequestDispatcher(String path) {
			this.rewritePath = path;
			return super.getRequestDispatcher(path);
		}
	};
}
