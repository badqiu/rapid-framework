package javacommon.util;


import javax.servlet.http.HttpServletRequest;

import cn.org.rapid_framework.page.PageRequest;
/**
 * 用于分页组件覆盖的类,新的分页组件覆盖此类的newPageRequest()方法以适合不同的分页创建
 * @author badqiu
 */
public class PageRequestFactory {
    public static final int DEFAULT_PAGE_SIZE = 10;
    static {
        System.out.println("PageRequestFactory.DEFAULT_PAGE_SIZE="+DEFAULT_PAGE_SIZE);
    }
    
    public static <T> PageRequest<T> bindPageRequest(PageRequest pr,HttpServletRequest request,String defaultSortColumns){
        return SimpleTablePageRequestFactory.bindPageRequest(pr,request,defaultSortColumns,DEFAULT_PAGE_SIZE);
    }
    
    public static PageRequest bindPageRequest(PageRequest pr,HttpServletRequest request,String defaultSortColumns,int defaultPageSize){
        return SimpleTablePageRequestFactory.bindPageRequest(pr,request,defaultSortColumns,defaultPageSize);
    }
}
