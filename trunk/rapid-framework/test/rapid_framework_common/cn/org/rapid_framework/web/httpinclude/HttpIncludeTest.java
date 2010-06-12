package cn.org.rapid_framework.web.httpinclude;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.tuckey.web.MockRequestDispatcher;

public class HttpIncludeTest extends TestCase {
	String cookie = "_javaeye3_session_=BAh7BzoMdXNlcl9pZGkCxEw6D3Nlc3Npb25faWQiJTg2NTRkNDgxNjhiYzhiY2RhODg1N2M3OTBjMGNkYTI5--a2a5c1d58579038336b581bab0ad2b53b4526ca5";
	public void test_remote_with_cookie() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.setCharacterEncoding("UTF-8");
		MockHttpServletRequest request = new MockHttpServletRequest();
		HttpInclude http = new HttpInclude(request, response);
		System.out.println(http.include("http://www.163.com"));
	}
	
	boolean includeExecuted = false;
	public void test_local_write_date_with_output_stream() throws UnsupportedEncodingException {
		final MockHttpServletResponse response = new MockHttpServletResponse();
		response.setCharacterEncoding("UTF-8");
		MockHttpServletRequest request = new MockHttpServletRequest(){
			@Override
			public RequestDispatcher getRequestDispatcher(final String path) {
				return new MockRequestDispatcher(path) {
					@Override
					public void include(ServletRequest servletRequest,ServletResponse servletResponse)throws ServletException, IOException {
						response.setIncludedUrl(path);
						new PrintStream(servletResponse.getOutputStream()).append("test_local_write_date_with_output_stream").flush();
//						super.include(servletRequest, servletResponse);
						includeExecuted = true;
					}
				};
			}
		};
		HttpInclude http = new HttpInclude(request, response);
		
		System.out.println(http.include("/userinfo/blog.htm"));
		System.out.println(response.getIncludedUrl());
//		System.out.println(response.getContentAsString());
		assertTrue(includeExecuted);
//		assertEquals(response.getContentAsString(),"test_local_write_date_with_output_stream");
		assertEquals(response.getIncludedUrl(),"/userinfo/blog.htm");
	}
	
	public void test_local_write_date_with_write() throws UnsupportedEncodingException {
		final MockHttpServletResponse response = new MockHttpServletResponse();
		response.setCharacterEncoding("UTF-8");
		MockHttpServletRequest request = new MockHttpServletRequest(){
			@Override
			public RequestDispatcher getRequestDispatcher(final String path) {
				return new MockRequestDispatcher(path) {
					@Override
					public void include(ServletRequest servletRequest,ServletResponse servletResponse)throws ServletException, IOException {
						response.setIncludedUrl(path);
						servletResponse.getWriter().append("test_local_write_date_with_write");
//						super.include(servletRequest, servletResponse);
						includeExecuted = true;
					}
				};
			}
		};
		HttpInclude http = new HttpInclude(request, response);
		
		System.out.println(http.include("/userinfo/blog.htm"));
		System.out.println(response.getIncludedUrl());
//		System.out.println(response.getContentAsString());
		assertTrue(includeExecuted);
//		assertEquals(response.getContentAsString(),"test_local_write_date_with_output_stream");
		assertEquals(response.getIncludedUrl(),"/userinfo/blog.htm");
	}
}
