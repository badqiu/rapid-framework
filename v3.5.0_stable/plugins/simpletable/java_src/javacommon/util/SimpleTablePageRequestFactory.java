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
	
	public static PageRequest<Map> newPageRequest(HttpServletRequest request,String defaultSortColumns) {
		return newPageRequest(request,defaultSortColumns,DEFAULT_PAGE_SIZE);
	}
	
	public static PageRequest<Map> newPageRequest(HttpServletRequest request,String defaultSortColumns,int defaultPageSize) {
		PageRequest<Map> pageRequest = new PageRequest();
		return bindPageRequestParameters(pageRequest, request,defaultSortColumns, defaultPageSize);
	}

	public static PageRequest<Map> bindPageRequestParameters(PageRequest<Map> pageRequest, HttpServletRequest request,String defaultSortColumns, int defaultPageSize) {
		pageRequest.setPageNumber(getIntParameter(request, "pageNumber", 1));
		pageRequest.setPageSize(getIntParameter(request, "pageSize", defaultPageSize));
		pageRequest.setSortColumns(getStringParameter(request, "sortColumns",defaultSortColumns));
		pageRequest.setFilters(WebUtils.getParametersStartingWith(request,"s_"));
		
		if(pageRequest.getPageSize() > MAX_PAGE_SIZE) {
			pageRequest.setPageSize(MAX_PAGE_SIZE);
		}
		return pageRequest;
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
