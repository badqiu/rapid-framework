package javacommon.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.util.WebUtils;

import cn.org.rapid_framework.page.PageRequest;

/**
 * @author badqiu
 */
public class SimpleTablePageHelper {
	
	private static final int MAX_PAGE_SIZE = 500;
	private static final int DEFAULT_PAGE_SIZE = 10;
	static {
		System.out.println("SimpleTablePageHelper.DEFAULT_PAGE_SIZE="+DEFAULT_PAGE_SIZE);
		System.out.println("SimpleTablePageHelper.MAX_PAGE_SIZE="+MAX_PAGE_SIZE);
	}
	
	public static PageRequest<Map> newPageRequest(HttpServletRequest request,String defaultSortColumn) {
		return newPageRequest(request,defaultSortColumn,DEFAULT_PAGE_SIZE);
	}
	
	public static PageRequest<Map> newPageRequest(HttpServletRequest request,String defaultSortColumn,int defaultPageSize) {
		PageRequest<Map> result = new PageRequest();
		result.setPageNumber(ServletRequestUtils.getIntParameter(request, "pageNumber", 1));
		result.setPageSize(ServletRequestUtils.getIntParameter(request, "pageSize", defaultPageSize));
		result.setSortColumns(ServletRequestUtils.getStringParameter(request, "sortColumns",defaultSortColumn));
		result.setFilters(WebUtils.getParametersStartingWith(request,"auto_include_"));
		
		if(result.getPageSize() > MAX_PAGE_SIZE) {
			result.setPageSize(MAX_PAGE_SIZE);
		}
		return result;
	}
	
}
