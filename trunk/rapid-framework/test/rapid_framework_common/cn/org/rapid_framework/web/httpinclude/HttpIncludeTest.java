package cn.org.rapid_framework.web.httpinclude;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import junit.framework.TestCase;

public class HttpIncludeTest extends TestCase {
	String cookie = "_javaeye3_session_=BAh7BzoMdXNlcl9pZGkCxEw6D3Nlc3Npb25faWQiJTg2NTRkNDgxNjhiYzhiY2RhODg1N2M3OTBjMGNkYTI5--a2a5c1d58579038336b581bab0ad2b53b4526ca5";
	public void test_remote_with_cookie() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.setCharacterEncoding("UTF-8");
		MockHttpServletRequest request = new MockHttpServletRequest();
		HttpInclude http = new HttpInclude(request, response);
		System.out.println(http.include("http://www.163.com"));
	}
	
	public void test_local_with_cookie() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.setCharacterEncoding("UTF-8");
		MockHttpServletRequest request = new MockHttpServletRequest();
		HttpInclude http = new HttpInclude(request, response);
		System.out.println(http.include("/userinfo/blog.htm"));
	}
}
