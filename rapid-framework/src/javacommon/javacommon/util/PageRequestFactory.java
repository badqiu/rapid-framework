package javacommon.util;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

import cn.org.rapid_framework.extremecomponents.ExtremeTablePage;
import cn.org.rapid_framework.extremecomponents.ExtremeTablePageRequestFactory;
import cn.org.rapid_framework.page.PageRequest;
/**
 * 用于分页组件覆盖的类,新的分页组件覆盖此类的newPageInfo()方法以适合不同的分页创建
 * @author badqiu
 */
public class PageRequestFactory {
	
	static int DEFAULT_PAGE_SIZE = 20;
	
	public static PageRequest newPageRequest(HttpServletRequest request,String defaultSortColumns){
		PageRequest info = ExtremeTablePageRequestFactory.createFromLimit(ExtremeTablePage.getLimit(request,DEFAULT_PAGE_SIZE),defaultSortColumns);
    	info.getFilters().putAll(WebUtils.getParametersStartingWith(request, "s_"));
    	return info;
    }
}
