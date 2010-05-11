package javacommon.util;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.extremecomponents.ExtremeTablePage;
import cn.org.rapid_framework.extremecomponents.ExtremeTablePageRequestFactory;
import cn.org.rapid_framework.page.PageRequest;
/**
 * 用于分页组件覆盖的类,新的分页组件覆盖此类的newPageRequest()方法以适合不同的分页创建
 * @author badqiu
 */
public class PageRequestFactory {
	
	static int DEFAULT_PAGE_SIZE = 10;
	static int MAX_PAGE_SIZE = 500;
	
	static{
		System.out.println("PageRequestFactory.DEFAULT_PAGE_SIZE="+DEFAULT_PAGE_SIZE);
		System.out.println("PageRequestFactory.MAX_PAGE_SIZE="+MAX_PAGE_SIZE);
	}
	
	public static <T> PageRequest<T> newPageRequest(HttpServletRequest request,String defaultSortColumns,T filters){
		PageRequest pr = newPageRequest(request,defaultSortColumns,DEFAULT_PAGE_SIZE);
		pr.setFilters(filters);
		return pr;
    }
	
	public static PageRequest newPageRequest(HttpServletRequest request,String defaultSortColumns){
		return newPageRequest(request,defaultSortColumns,DEFAULT_PAGE_SIZE);
    }
	
	public static PageRequest newPageRequest(HttpServletRequest request,String defaultSortColumns,int defaultPageSize){
		PageRequest<Map> result = ExtremeTablePageRequestFactory.createFromLimit(ExtremeTablePage.getLimit(request,defaultPageSize),defaultSortColumns);
    	
		Map autoIncludeFilters = WebUtils.getParametersStartingWith(request, "s_");
		result.getFilters().putAll(autoIncludeFilters);
		
    	if(result.getPageSize() > MAX_PAGE_SIZE) {
    		result.setPageSize(MAX_PAGE_SIZE);
    	}
    	return result;
    }
}
