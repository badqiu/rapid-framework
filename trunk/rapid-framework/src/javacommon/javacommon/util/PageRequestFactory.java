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
	
	public static PageRequest newPageRequest(HttpServletRequest request,String defaultSortColumn,String defaultSortDirection){
    	PageRequest info = ExtremeTablePageRequestFactory.createFromLimit(ExtremeTablePage.getLimit(request),defaultSortColumn,defaultSortDirection);
    	info.getFilters().putAll(WebUtils.getParametersStartingWith(request, "s_"));
    	return info;
    }
}
