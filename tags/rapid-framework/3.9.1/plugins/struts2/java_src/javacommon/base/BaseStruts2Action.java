package javacommon.base;

import java.util.Map;

import javacommon.util.ConvertRegisterHelper;
import javacommon.util.PageRequestFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.springframework.util.Assert;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import cn.org.rapid_framework.util.ObjectUtils;

import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseStruts2Action extends ActionSupport implements RequestAware {
	protected final static String CREATED_SUCCESS = "创建成功";
	protected final static String UPDATE_SUCCESS = "更新成功";
	protected final static String DELETE_SUCCESS = "删除成功";
	
	protected Map requestMap = null;
	static {
		//注册converters
		ConvertRegisterHelper.registerConverters();
	}
	
	public void copyProperties(Object target,Object source) {
		BeanUtils.copyProperties(target, source);
	}

	public <T> T copyProperties(Class<T> destClass,Object orig) {
		return BeanUtils.copyProperties(destClass, orig);
	}
	
	public void setRequest(Map request) {
		this.requestMap = request;
	}

	public void savePage(Page page,PageRequest pageRequest){
		savePage("",page,pageRequest);
	}
	
	/**
	 * 用于一个页面有多个extremeTable是使用
	 * @param tableId 等于extremeTable的tableId属性
	 */
	public void savePage(String tableId,Page page,PageRequest pageRequest){
		Assert.notNull(tableId,"tableId must be not null");
		Assert.notNull(page,"page must be not null");
		
		getRequest().setAttribute(tableId+"page", page);
		getRequest().setAttribute(tableId+"totalRows", new Integer(page.getTotalCount()));
		getRequest().setAttribute(tableId+"pageRequest", pageRequest);
		getRequest().setAttribute(tableId+"query", pageRequest);
	}
	
	public <T extends PageRequest> T newQuery(Class<T> queryClazz,String defaultSortColumns){
		PageRequest query = PageRequestFactory.bindPageRequest(org.springframework.beans.BeanUtils.instantiateClass(queryClazz),ServletActionContext.getRequest(),defaultSortColumns);
		return (T)query;
    }
	
	public boolean isNullOrEmptyString(Object o) {
		return ObjectUtils.isNullOrEmptyString(o);
	}
	
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	
}

