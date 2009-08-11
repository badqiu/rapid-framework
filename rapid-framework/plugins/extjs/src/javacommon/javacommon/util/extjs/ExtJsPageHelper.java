package javacommon.util.extjs;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import cn.org.rapid_framework.page.PageRequest;

public class ExtJsPageHelper {
	static int DEFAULT_PAGE_SIZE = 10;
	
	public static PageRequest<Map> createPageRequestForExtJs(HttpServletRequest request,String defaultOrderBy) {
		int start = getIntParameter(request,"start",0);
		int limit = getIntParameter(request, "limit", DEFAULT_PAGE_SIZE);
		
		String field_type = request.getParameter("field_type");
		String query = request.getParameter("query"); //SearchField.js 里设置此参数名
		String sort = request.getParameter("sort");// 指定排序的列
		String dir = request.getParameter("dir");// 顺序倒序
		String orderBy = defaultOrderBy; //默认正向排序列 
		if (sort != null && dir != null){
			orderBy = sort + " " + dir;
		}
		PageRequest result = new PageRequest();
		//如果没有按照指定字段搜索,则按全条件查询
		if(field_type!=null){
			Map map = new HashMap();
			map.put(field_type, query);
			result.setFilters(map);
		}
		
		result.setPageNumber(start / limit + 1);
		result.setPageSize(limit);
		result.setSortColumns(orderBy);
		return result;
	}
	
	private static int getIntParameter(HttpServletRequest request,String name,int defaultValue) {
		String value = request.getParameter(name);
		if (StringUtils.isNotBlank(value)) {
			return Integer.valueOf(value);
		}
		return defaultValue;
	}
}
