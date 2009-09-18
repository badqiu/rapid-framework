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
public class UserInfoController extends BaseSpringController{
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	private UserInfoManager userInfoManager;
	
	private final String LIST_ACTION = "redirect:/userinfo.do";
	
	public UserInfoController() {
	}
	
	/** 
	 * 通过spring自动注入
	 **/
	public void setUserInfoManager(UserInfoManager manager) {
		this.userInfoManager = manager;
	}
	
	/** 列表 */
	@RequestMapping(value="/userinfo",method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response,UserInfo userInfo) {
		long start = System.currentTimeMillis();
		PageRequest<Map> pageRequest = newPageRequest(request,DEFAULT_SORT_COLUMNS);
		//pageRequest.getFilters(); //add custom filters
		
		Page page = this.userInfoManager.findByPageRequest(pageRequest);
		savePage(page,pageRequest,request);
		System.out.println(System.currentTimeMillis() - start);
		return new ModelAndView("/pages/userinfo/list","userInfo",userInfo);
	}
	
	/** 进入新增 */
	@RequestMapping(value="/userinfo/new",method=RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request,HttpServletResponse response,UserInfo userInfo) throws Exception {
		return new ModelAndView("/pages/userinfo/create","userInfo",userInfo);
	}
	
	/** 显示 */
	@RequestMapping(value="/userinfo/{id}",method=RequestMethod.GET)
	public ModelAndView show(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		UserInfo userInfo = (UserInfo)userInfoManager.getById(id);
		return new ModelAndView("/pages/userinfo/show","userInfo",userInfo);
	}
	
	/** 编辑 */
	@RequestMapping(value="/userinfo/{id}/edit",method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		UserInfo userInfo = (UserInfo)userInfoManager.getById(id);
		return new ModelAndView("/pages/userinfo/edit","userInfo",userInfo);
	}
	
	/** 保存新增 */
	@RequestMapping(value="/userinfo",method=RequestMethod.POST)
	public ModelAndView create(HttpServletRequest request,HttpServletResponse response,UserInfo userInfo) throws Exception {
		userInfoManager.save(userInfo);
		return new ModelAndView(LIST_ACTION);
	}
	
	/** 保存更新 */
	@RequestMapping(value="/userinfo/{id}",method=RequestMethod.PUT)
	public ModelAndView update(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		UserInfo userInfo = (UserInfo)userInfoManager.getById(id);
		bind(request,userInfo);
		userInfoManager.update(userInfo);
		return new ModelAndView(LIST_ACTION);
	}
	
	/** 删除 */
	@RequestMapping(value="/userinfo/{id}",method=RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable Long id,HttpServletRequest request,HttpServletResponse response) {
		userInfoManager.removeById(id);
		return new ModelAndView(LIST_ACTION);
	}

	/** 批量删除 */
	@RequestMapping(value="/userinfo",method=RequestMethod.DELETE)
	public ModelAndView batchDelete(HttpServletRequest request,HttpServletResponse response) {
		String[] items = request.getParameterValues("items");
		for(int i = 0; i < items.length; i++) {
			java.lang.Long id = new java.lang.Long(items[i]);
			userInfoManager.removeById(id);
		}
		return new ModelAndView(LIST_ACTION);
	}
}

