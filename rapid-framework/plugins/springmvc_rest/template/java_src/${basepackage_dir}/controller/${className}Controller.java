/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.company.project.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.org.rapid_framework.beanutils.BeanUtils;

import java.util.*;

import javacommon.base.*;
import javacommon.util.*;

import cn.org.rapid_framework.util.*;
import cn.org.rapid_framework.web.util.*;
import cn.org.rapid_framework.page.*;
import cn.org.rapid_framework.page.impl.*;

import com.company.project.model.*;
import com.company.project.dao.*;
import com.company.project.service.*;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping("/userinfo")
public class UserInfoController extends BaseSpringController{
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	private UserInfoManager userInfoManager;
	
	private final String LIST_ACTION = "redirect:/userinfo";
	
	public UserInfoController() {
	}
	
	/** 
	 * 通过spring自动注入
	 **/
	public void setUserInfoManager(UserInfoManager manager) {
		this.userInfoManager = manager;
	}
	
	/** 列表 */
	@RequestMapping
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response,UserInfo userInfo) {
		PageRequest<Map> pageRequest = newPageRequest(request,DEFAULT_SORT_COLUMNS);
		//pageRequest.getFilters(); //add custom filters
		
		Page page = this.userInfoManager.findByPageRequest(pageRequest);
		savePage(page,pageRequest,request);
		return new ModelAndView("/userinfo/list","userInfo",userInfo);
	}
	
	/** 进入新增 */
	@RequestMapping(value="/new")
	public ModelAndView _new(HttpServletRequest request,HttpServletResponse response,UserInfo userInfo) throws Exception {
		return new ModelAndView("/userinfo/new","userInfo",userInfo);
	}
	
	/** 显示 */
	@RequestMapping(value="/{id}")
	public ModelAndView show(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		UserInfo userInfo = (UserInfo)userInfoManager.getById(id);
		return new ModelAndView("/userinfo/show","userInfo",userInfo);
	}
	
	/** 编辑 */
	@RequestMapping(value="/{id}/edit")
	public ModelAndView edit(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		UserInfo userInfo = (UserInfo)userInfoManager.getById(id);
		return new ModelAndView("/userinfo/edit","userInfo",userInfo);
	}
	
	/** 保存新增 */
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView create(HttpServletRequest request,HttpServletResponse response,UserInfo userInfo) throws Exception {
		userInfoManager.save(userInfo);
		return new ModelAndView(LIST_ACTION);
	}
	
	/** 保存更新 */
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public ModelAndView update(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		UserInfo userInfo = (UserInfo)userInfoManager.getById(id);
		bind(request,userInfo);
		userInfoManager.update(userInfo);
		return new ModelAndView(LIST_ACTION);
	}
	
	/** 删除 */
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) {
		userInfoManager.removeById(id);
		return new ModelAndView(LIST_ACTION);
	}

	/** 批量删除 */
	@RequestMapping(method=RequestMethod.DELETE)
	public ModelAndView batchDelete(HttpServletRequest request,HttpServletResponse response) {
		String[] items = request.getParameterValues("items");
		for(int i = 0; i < items.length; i++) {
			java.lang.Long id = new java.lang.Long(items[i]);
			userInfoManager.removeById(id);
		}
		return new ModelAndView(LIST_ACTION);
	}
	
}

