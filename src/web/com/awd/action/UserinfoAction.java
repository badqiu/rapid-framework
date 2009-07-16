/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javacommon.util.ListRange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import cn.org.rapid_framework.generator.provider.java.model.JavaClass;
import cn.org.rapid_framework.generator.provider.java.model.JavaField;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.awd.model.Userinfo;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.ModelDriven;

import java.util.*;

import javacommon.base.*;
import javacommon.util.*;

import cn.org.rapid_framework.util.*;
import cn.org.rapid_framework.web.util.*;
import cn.org.rapid_framework.page.*;
import cn.org.rapid_framework.page.impl.*;

import com.awd.model.*;
import com.awd.dao.*;
import com.awd.service.*;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */


public class UserinfoAction extends BaseStruts2Action implements Preparable,ModelDriven{
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	//forward paths
	protected static final String QUERY_JSP = "/pages/Userinfo/query.jsp";
	protected static final String LIST_JSP= "/pages/Userinfo/list.jsp";
	protected static final String CREATE_JSP = "/pages/Userinfo/create.jsp";
	protected static final String EDIT_JSP = "/pages/Userinfo/edit.jsp";
	protected static final String SHOW_JSP = "/pages/Userinfo/show.jsp";
	//redirect paths,startWith: !
	protected static final String LIST_ACTION = "!/pages/Userinfo/list.do";
	
	private UserinfoManager userinfoManager;
	
	private Userinfo userinfo;
	java.lang.Long id = null;
	private String[] items;

	public void prepare() throws Exception {
		if (isNullOrEmptyString(id)) {
			userinfo = new Userinfo();
		} else {
			userinfo = (Userinfo)userinfoManager.getById(id);
		}
	}
	
	/** 通过spring自动注入 */
	public void setUserinfoManager(UserinfoManager manager) {
		this.userinfoManager = manager;
	}	
	
	public Object getModel() {
		return userinfo;
	}
	
	public void setId(java.lang.Long val) {
		this.id = val;
	}

	public void setItems(String[] items) {
		this.items = items;
	}

	/** 进入查询页面 */
	public String query() {
		return QUERY_JSP;
	}
	
	/** 执行搜索 */
	public String list() {
		PageRequest pageRequest = newPageRequest(DEFAULT_SORT_COLUMNS);
		//pageRequest.getFilters().put("key",value);     //add custom filter
		Page page = userinfoManager.findByPageRequest(pageRequest);
		savePage(page);
		return LIST_JSP;
	}
	
	/** 查看对象*/
	public String show() {
		return SHOW_JSP;
	}
	
	/** 进入新增页面*/
	public String create() {
		return CREATE_JSP;
	}
	
	/** 保存新增对象 */
	public String save() {
		userinfoManager.save(userinfo);
		return LIST_ACTION;
	}
	
	/**进入更新页面*/
	public String edit() {
		return EDIT_JSP;
	}
	
	/**保存更新对象*/
	public String update() {
		userinfoManager.update(this.userinfo);
		return LIST_ACTION;
	}
	
	/**删除对象*/
	public String delete() {
		for(int i = 0; i < items.length; i++) {
			Hashtable params = HttpUtils.parseQueryString(items[i]);
			java.lang.Long id = new java.lang.Long((String)params.get("id"));
			userinfoManager.removeById(id);
		}
		return LIST_ACTION;
	}

	/**
	 * ExtGrid使用
	 * 列表
	 * @throws IOException
	 */
	public void extlist() throws IOException
	{
		int DEFAULT_PAGE_SIZE = 10;
		String strStart = getRequest().getParameter("start");
		String strLimit = getRequest().getParameter("limit");
		String field_type = getRequest().getParameter("field_type");
		String query = getRequest().getParameter("query"); //SearchField.js 里设置此参数名
		String sort = getRequest().getParameter("sort");// 指定排序的列
		String dir = getRequest().getParameter("dir");// 顺序倒序
		String orderBy = "id"; //默认正向排序列 
		if (sort != null && dir != null)
		{
			orderBy = sort + " " + dir;
		}
		PageRequest result = new PageRequest();
		//如果没有按照指定字段搜索,则按全条件查询
		if(field_type==null){
			result = newPageRequest(DEFAULT_SORT_COLUMNS);
		}else{
			Map map = new HashMap();
			map.put(field_type, query);
			result.setFilters(map);
		}
		int start = 0;
		int limit = DEFAULT_PAGE_SIZE;
		if (StringUtils.isNotBlank(strStart))
			start = Integer.valueOf(strStart);
		if (StringUtils.isNotBlank(strLimit))
			limit = Integer.valueOf(strLimit);
		
		result.setPageNumber(start / limit + 1);
		result.setPageSize(limit);
		result.setSortColumns(orderBy);
		Page page = userinfoManager.findByPageRequest(result);
		
		List<Userinfo> Userinfolist = (List) page.getResult();
		ListRange<Userinfo> resultList = new ListRange<Userinfo>();
		resultList.setList(Userinfolist);
		resultList.setTotalSize(page.getTotalCount());
		resultList.setMessage("ok");
		resultList.setSuccess(true);
		outJson(resultList);
	}

	/**
	 * extGrid保存
	 * @throws IOException
	 */
	public void extsave() throws IOException
	{
		Map<String, Object> result = new HashMap<String, Object>();
		try
		{
			userinfoManager.save(userinfo);
			result.put("success", true);
			result.put("msg", "添 加 成 功!");
		}
		catch (RuntimeException e)
		{
			result.put("failure", true);
			result.put("msg", e);
			e.printStackTrace();
		}
		outJson(result);
	}
	
	/**
	 * extGrid修改
	 * @throws IOException
	 */
	public void extupdate() throws IOException
	{
		Map<String, Object> result = new HashMap<String, Object>();
		try
		{
			userinfoManager.save(userinfo);
			result.put("success", true);
			result.put("msg", "修 改 成 功!");
		}
		catch (RuntimeException e)
		{
			result.put("failure", true);
			result.put("msg", e);
			e.printStackTrace();
		}
		outJson(result);
	}
	
	/**
	 * extGrid删除
	 * @throws IOException
	 */
	public void extdelete() throws IOException
	{
		String ids = getRequest().getParameter("ids");
		String[] idarray = ids.split(",");
		Map<String, Object> result = new HashMap<String, Object>();
		try
		{
			for (int i = 0; i < idarray.length; i++)
			{
				java.lang.Long id = new java.lang.Long((String)idarray[i]);
				userinfoManager.removeById(id);
			}
			result.put("success", true);
			result.put("msg", "删除成功");
		}
		catch (RuntimeException e)
		{
			result.put("failure", true);
			result.put("msg", e);
			e.printStackTrace();
		}
		outJson(result);
	}
	
	
	/**
	 * extGrid搜索范围下拉框 
	 * @throws IOException
	 */
	public void extfield() throws IOException
	{
		List result = new ArrayList(); 
		Class c = Userinfo.class;
		JavaClass javaClass = new JavaClass(c);
		try
		{
			List list = javaClass.getFields();
			for (int i = 0; i < list.size(); i++)
			{
				JavaField javaField = (JavaField)list.get(i);
				if (javaField.getFieldName().startsWith("ALIAS"))
				{
				        String str = c.getDeclaredField(javaField.getFieldName()).get(null).toString();
				        if (!javaField.getFieldName().substring(6).toLowerCase().equals("id"))
				        {
					        Map map = new HashMap();
					        map.put("id",i);
							map.put("name",str);
							map.put("code",javaField.getFieldName().substring(6).toLowerCase());
							result.add(map);
						}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		outJsonArray(result);
	}
}
