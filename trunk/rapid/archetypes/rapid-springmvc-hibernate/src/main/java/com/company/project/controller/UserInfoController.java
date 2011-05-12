/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2011
 */


package com.company.project.controller;

import java.util.Map;

import java.text.SimpleDateFormat;
import java.util.Date;

import common.base.BaseRestSpringController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.web.scope.Flash;

import java.util.*;
import common.base.*;
import common.util.*;

import cn.org.rapid_framework.util.*;
import cn.org.rapid_framework.web.util.*;
import cn.org.rapid_framework.util.page.*;
import cn.org.rapid_framework.page.*;


import com.company.project.domain.*;
import com.company.project.dao.*;
import com.company.project.service.*;
import com.company.project.query.*;

/**
 * @author badqiu
 * @author hunhun
 * @since 4.0 
 */

@Controller
@RequestMapping("/userinfo")
public class UserInfoController extends BaseRestSpringController<UserInfo,java.lang.Long>{
	//默认多列排序,example: username desc,createTime asc	
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	private UserInfoManager userInfoManager;
	
	private final String LIST_ACTION = "redirect:/userinfo";
	
	/** 
	 * 增加setXXXX()方法,spring就可以通过autowire自动设置对象属性,注意大小写
	 **/
	public void setUserInfoManager(UserInfoManager manager) {
		this.userInfoManager = manager;
	}
	
	/** binder用于bean属性的设置 */
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
	        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));  
	}
	   
	/**
	 * 增加了@ModelAttribute的方法可以在本controller方法调用前执行,可以存放一些共享变量,如枚举值,或是一些初始化操作
	 */
	@ModelAttribute
	public void init(ModelMap model) {
		model.put("now", new java.sql.Timestamp(System.currentTimeMillis()));
	}
	
	/** 列表 */
	@RequestMapping
	public String index(ModelMap model,UserInfoQuery query,HttpServletRequest request,HttpServletResponse response) {
		Page page = this.userInfoManager.findPage(query);
		
		model.addAllAttributes(toModelMap(page, query));
		return "/userinfo/index";
	}
	
	/** 显示 */
	@RequestMapping(value="/{id}")
	public String show(ModelMap model,@PathVariable java.lang.Long id) throws Exception {
		UserInfo userInfo = (UserInfo)userInfoManager.getById(id);
		model.addAttribute("userInfo",userInfo);
		return "/userinfo/show";
	}

	/** 进入新增 */
	@RequestMapping(value="/new")
	public String _new(ModelMap model,UserInfo userInfo,HttpServletRequest request,HttpServletResponse response) throws Exception {
		model.addAttribute("userInfo",userInfo);
		return "/userinfo/new";
	}
	
	/** 保存新增,@Valid标注spirng在绑定对象时自动为我们验证对象属性并存放errors在BindingResult  */
	@RequestMapping(method=RequestMethod.POST)
	public String create(ModelMap model,@Valid UserInfo userInfo,BindingResult errors,HttpServletRequest request,HttpServletResponse response) throws Exception {
		if(errors.hasErrors()) {
			return  "/userinfo/new";
		}
		
		userInfoManager.save(userInfo);
		Flash.current().success(CREATED_SUCCESS); //存放在Flash中的数据,在下一次http请求中仍然可以读取数据,error()用于显示错误消息
		return LIST_ACTION;
	}
	
	/** 编辑 */
	@RequestMapping(value="/{id}/edit")
	public String edit(ModelMap model,@PathVariable java.lang.Long id) throws Exception {
		UserInfo userInfo = (UserInfo)userInfoManager.getById(id);
		model.addAttribute("userInfo",userInfo);
		return "/userinfo/edit";
	}
	
	/** 保存更新,@Valid标注spirng在绑定对象时自动为我们验证对象属性并存放errors在BindingResult  */
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public String update(ModelMap model,@PathVariable java.lang.Long id,@Valid UserInfo userInfo,BindingResult errors,HttpServletRequest request,HttpServletResponse response) throws Exception {
		if(errors.hasErrors()) {
			return "/userinfo/edit";
		}
		
		userInfoManager.update(userInfo);
		Flash.current().success(UPDATE_SUCCESS);
		return LIST_ACTION;
	}
	
	/** 删除 */
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public String delete(ModelMap model,@PathVariable java.lang.Long id) {
		userInfoManager.removeById(id);
		Flash.current().success(DELETE_SUCCESS);
		return LIST_ACTION;
	}

	/** 批量删除 */
	@RequestMapping(method=RequestMethod.DELETE)
	public String batchDelete(ModelMap model,@RequestParam("items") java.lang.Long[] items) {
		for(int i = 0; i < items.length; i++) {
			userInfoManager.removeById(items[i]);
		}
		Flash.current().success(DELETE_SUCCESS);
		return LIST_ACTION;
	}
	
}

