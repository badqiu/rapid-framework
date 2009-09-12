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
	
	public static PageRequest newPageRequest(HttpServletRequest request,String defaultSortColumns){
		return SimpleTablePageRequestFactory.newPageRequest(request,defaultSortColumns);
    }
	
	public static PageRequest newPageRequest(HttpServletRequest request,String defaultSortColumns,int defaultPageSize){
		return SimpleTablePageRequestFactory.newPageRequest(request, defaultSortColumns,defaultPageSize);
    }
}
