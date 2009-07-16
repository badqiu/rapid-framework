/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

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


@Controller
public class UserinfoController extends BaseSpringController{
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	private UserinfoManager userinfoManager;
	
	private final String LIST_ACTION = "redirect:/pages/Userinfo/list.do";
	
	public UserinfoController() {
	}
	
	/** 
	 * 通过spring自动注入
	 **/
	public void setUserinfoManager(UserinfoManager manager) {
		this.userinfoManager = manager;
	}
	
	/** 
	 * 进入查询页面
	 **/
	public ModelAndView query(HttpServletRequest request,HttpServletResponse response) {
		return new ModelAndView("/pages/Userinfo/query");
	}
	
	/** 
	 * 执行搜索 
	 **/
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) {
		PageRequest pageRequest = newPageRequest(request,DEFAULT_SORT_COLUMNS);
		Page page = this.userinfoManager.findByPageRequest(pageRequest);
		savePage(page, request);
		return new ModelAndView("/pages/Userinfo/list_spring");
	}
	
	/** 
	 * 查看对象
	 **/
	public ModelAndView show(HttpServletRequest request,HttpServletResponse response) throws Exception {
		java.lang.Long id = new java.lang.Long(request.getParameter("id"));
		Userinfo userinfo = (Userinfo)userinfoManager.getById(id);
		return new ModelAndView("/pages/Userinfo/show","userinfo",userinfo);
	}
	
	/** 
	 * 进入新增页面
	 **/
	public ModelAndView create(HttpServletRequest request,HttpServletResponse response,Userinfo userinfo) throws Exception {
		return new ModelAndView("/pages/Userinfo/create","userinfo",userinfo);
	}
	
	/** 
	 * 保存新增对象
	 **/
	public ModelAndView save(HttpServletRequest request,HttpServletResponse response,Userinfo userinfo) throws Exception {
		userinfoManager.save(userinfo);
		return new ModelAndView(LIST_ACTION);
	}
	
	/**
	 * 进入更新页面
	 **/
	public ModelAndView edit(HttpServletRequest request,HttpServletResponse response) throws Exception {
		java.lang.Long id = new java.lang.Long(request.getParameter("id"));
		Userinfo userinfo = (Userinfo)userinfoManager.getById(id);
		return new ModelAndView("/pages/Userinfo/edit","userinfo",userinfo);
	}
	
	/**
	 * 保存更新对象
	 **/
	public ModelAndView update(HttpServletRequest request,HttpServletResponse response) throws Exception {
		java.lang.Long id = new java.lang.Long(request.getParameter("id"));
		
		Userinfo userinfo = (Userinfo)userinfoManager.getById(id);
		bind(request,userinfo);
		userinfoManager.update(userinfo);
		return new ModelAndView(LIST_ACTION);
	}
	
	/**
	 *删除对象
	 **/
	public ModelAndView delete(HttpServletRequest request,HttpServletResponse response) {
		String[] items = request.getParameterValues("items");
		for(int i = 0; i < items.length; i++) {
			Hashtable params = HttpUtils.parseQueryString(items[i]);
			
			java.lang.Long id = new java.lang.Long((String)params.get("id"));
			
			userinfoManager.removeById(id);
		}
		return new ModelAndView(LIST_ACTION);
	}
	
}

