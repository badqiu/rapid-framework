package javacommon.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.WebUtils;

import cn.org.rapid_framework.page.PageRequest;

/**
 * @author badqiu
 */
public class SimpleTablePageRequestFactory {
	
	private static final int MAX_PAGE_SIZE = 500;
	private static final int DEFAULT_PAGE_SIZE = 10;
	static {
		System.out.println("SimpleTablePageRequestFactory.DEFAULT_PAGE_SIZE="+DEFAULT_PAGE_SIZE);
		System.out.println("SimpleTablePageRequestFactory.MAX_PAGE_SIZE="+MAX_PAGE_SIZE);
	}
	
	public static PageRequest<Map> newPageRequest(HttpServletRequest request,String defaultSortColumn) {
		return newPageRequest(request,defaultSortColumn,DEFAULT_PAGE_SIZE);
	}
	
	public static PageRequest<Map> newPageRequest(HttpServletRequest request,String defaultSortColumn,int defaultPageSize) {
		PageRequest<Map> result = new PageRequest();
		result.setPageNumber(getIntParameter(request, "pageNumber", 1));
		result.setPageSize(getIntParameter(request, "pageSize", defaultPageSize));
		result.setSortColumns(getStringParameter(request, "sortColumns",defaultSortColumn));
		result.setFilters(WebUtils.getParametersStartingWith(request,"auto_include_"));
		
		if(result.getPageSize() > MAX_PAGE_SIZE) {
			result.setPageSize(MAX_PAGE_SIZE);
		}
		return result;
	}
	
	static String getStringParameter(HttpServletRequest request,String paramName, String defaultValue) {
		String value = request.getParameter(paramName);
		return StringUtils.isEmpty(value) ? defaultValue : value;
	}

	static int getIntParameter(HttpServletRequest request,String paramName,int defaultValue) {
		String value = request.getParameter(paramName);
		return StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
	}
	
}
