package javacommon.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javacommon.util.ConvertRegisterHelper;
import javacommon.util.PageRequestFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.springframework.util.Assert;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import cn.org.rapid_framework.util.ObjectUtils;

import com.opensymphony.oscache.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseStruts2Action extends ActionSupport implements RequestAware {
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

	public void savePage(Page page){
		savePage("",page);
	}
	
	public void outJsonString(String str) {
		getResponse().setContentType("text/javascript;charset=UTF-8");
		outString(str);
	}

	public void outJson(Object obj) {
		outJsonString(JSONObject.fromObject(obj).toString());
	}

	public void outJsonArray(Object array) {
		outJsonString(JSONArray.fromObject(array).toString());
	}
	
	public void outString(String str) {
		try {
			PrintWriter out = getResponse().getWriter();
			out.write(str);
		} catch (IOException e) {
		}
	}

	public void outXMLString(String xmlStr) {
		getResponse().setContentType("application/xml;charset=UTF-8");
		outString(xmlStr);
	}
	
	/**
	 * 用于一个页面有多个extremeTable是使用
	 * @param tableId 等于extremeTable的tableId属性
	 */
	public void savePage(String tableId,Page page){
		Assert.notNull(tableId);
		requestMap.put(tableId+"page", page);
		requestMap.put(tableId+"totalRows", new Integer(page.getTotalCount()));
	}
	
	public PageRequest newPageRequest(String defaultSortColumns){
		return PageRequestFactory.newPageRequest(ServletActionContext.getRequest(), defaultSortColumns);
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

