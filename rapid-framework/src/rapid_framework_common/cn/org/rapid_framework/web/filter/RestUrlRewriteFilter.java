package cn.org.rapid_framework.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class RestUrlRewriteFilter extends OncePerRequestFilter implements Filter{
	private static final String DEFAULT_EXECUDE_EXTENTIONS = "jsp,jspx,do";
	private static final String DEFAULT_PREFIX = "/static";
	
	private String prefix = null;
	private boolean debug = false;
	private String[] excludeExtentions = null;
	
	protected void initFilterBean() throws ServletException {
		initParameter(getFilterConfig());
	}

	private void initParameter(FilterConfig filterConfig) {
		prefix = getStringParameter(filterConfig,"prefix",DEFAULT_PREFIX);
		debug = getBooleanParameter(filterConfig,"debug",false);
		String excludesString = getStringParameter(filterConfig,"excludeExtentions",DEFAULT_EXECUDE_EXTENTIONS);
		excludeExtentions = excludesString.split(",");
		
		System.out.println();
		System.out.println("RestUrlRewriteFilter.prefix=["+prefix+"] will rewrite url from /demo.html => ${prefix}/demo.html by forward");
		System.out.println("RestUrlRewriteFilter.excludeExtentions=["+excludesString+"] will not rewrite url");
		System.out.println("RestUrlRewriteFilter.debug="+debug);
		System.out.println();
	}

	protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
		String from = request.getRequestURI().substring(request.getContextPath().length());
		String extension = FilenameUtils.getExtension(from);
		if(rewriteURL(extension)) {
			final String to = prefix+from;
			if(debug) {
				System.out.println("RestUrlRewriteFilter: forward request from "+from+" to "+to);
			}
			request.getRequestDispatcher(to).forward(request, response);
		}else {
			if(debug) {
				System.out.println("RestUrlRewriteFilter: not rewrite url:"+from);
			}
			filterChain.doFilter(request, response);
			return;
		}		
	}
	
	private boolean rewriteURL(String requestURIExcension) {
		if("".equals(requestURIExcension)) {
			return false;
		}
		
		for(int i = 0; i < excludeExtentions.length; i++) {
			if(excludeExtentions[i].equals(requestURIExcension)) {
				return false;
			}
		}
		return true;
	}

	private String getStringParameter(FilterConfig filterConfig,String name,String defaultValue) {
		String value = filterConfig.getInitParameter(name);
		if(value == null || "".equals(value.trim())) {
			return defaultValue;
		}
		return value;
	}
	
	private boolean getBooleanParameter(FilterConfig filterConfig,String name,boolean defaultValue) {
		String value = getStringParameter(filterConfig, name, String.valueOf(defaultValue));
		return Boolean.parseBoolean(value);
	}
	
}
