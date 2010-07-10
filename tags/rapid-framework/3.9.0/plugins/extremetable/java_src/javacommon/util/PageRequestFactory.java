package javacommon.util;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.extremecomponents.table.limit.Filter;
import org.extremecomponents.table.limit.Limit;
import org.extremecomponents.table.limit.Sort;

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
	
	public static <T> PageRequest<T> bindPageRequest(PageRequest pr,HttpServletRequest request,String defaultSortColumns){
		return  bindPageRequest(pr,request,defaultSortColumns,DEFAULT_PAGE_SIZE);
    }
	
	public static PageRequest bindPageRequest(PageRequest pr,HttpServletRequest request,String defaultSortColumns,int defaultPageSize){
		Limit limit = ExtremeTablePage.getLimit(request,defaultPageSize);
		ExtremeTablePageRequestFactory.bindPageRequest(pr,limit,defaultSortColumns);
    	if(pr.getPageSize() > MAX_PAGE_SIZE) {
    		pr.setPageSize(MAX_PAGE_SIZE);
    	}
    	return pr;
    }
	
}
