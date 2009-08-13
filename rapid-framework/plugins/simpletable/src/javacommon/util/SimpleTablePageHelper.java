package javacommon.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

import cn.org.rapid_framework.page.PageRequest;

public class SimpleTablePageHelper {
	
	private static final int DEFAULT_PAGE_SIZE = 10;

	public static PageRequest<Map> newPageRequest(HttpServletRequest request,String defaultSortColumn) {
		return newPageRequest(request, DEFAULT_PAGE_SIZE,defaultSortColumn);
	}
	
	public static PageRequest<Map> newPageRequest(HttpServletRequest request,int defaultPageSize,String defaultSortColumn) {
		PageRequest<Map> result = new PageRequest(new HashMap());
		result.setPageNumber(ServletRequestUtils.getIntParameter(request, "pageNumber", 1));
		result.setPageSize(ServletRequestUtils.getIntParameter(request, "pageSize", defaultPageSize));
		result.setSortColumns(ServletRequestUtils.getStringParameter(request, "sortColumns",defaultSortColumn));
		return result;
	}
	
}
