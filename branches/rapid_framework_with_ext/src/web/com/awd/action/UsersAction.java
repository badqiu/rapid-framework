/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.action;

import java.util.Hashtable;
import java.util.List;

import javacommon.base.BaseStruts2Action;
import javacommon.util.HibernateWebUtils;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import cn.org.rapid_framework.web.util.HttpUtils;

import com.awd.model.Menus;
import com.awd.model.Roles;
import com.awd.model.Users;
import com.awd.service.MenusManager;
import com.awd.service.RolesManager;
import com.awd.service.UsersManager;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */


public class UsersAction extends BaseStruts2Action implements Preparable,ModelDriven{
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	//forward paths
	protected static final String QUERY_JSP = "/pages/Users/query.jsp";
	protected static final String LIST_JSP= "/pages/Users/list.jsp";
	protected static final String CREATE_JSP = "/pages/Users/create.jsp";
	protected static final String EDIT_JSP = "/pages/Users/edit.jsp";
	protected static final String SHOW_JSP = "/pages/Users/show.jsp";
	//redirect paths,startWith: !
	protected static final String LIST_ACTION = "!/pages/Users/list.do";
	
	private UsersManager usersManager;
	private RolesManager rolesManager;
	private MenusManager menusManager;
	
	private Users users;
	java.lang.Long id = null;
	private String[] items;
	
	private List<Roles> allRoles; //全部可�用�角色列�表
	private List<Menus> allMenus; //全部可�用�角色列�表
	private List<Long> checkedRoleIds; //页面中钩选的角色id列表
	private List<Long> checkedMenuIds; //页面中钩选的角色id列表


	public void prepare() throws Exception {
		if (isNullOrEmptyString(id)) {
			users = new Users();
		} else {
			users = (Users)usersManager.getById(id);
		}
	}
	
	/** 通过spring自动注入 */
	public void setUsersManager(UsersManager manager) {
		this.usersManager = manager;
	}	
	
	/** 通过spring自动注入 */
	public void setRolesManager(RolesManager manager) {
		this.rolesManager = manager;
	}
	

	
	public Object getModel() {
		return users;
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
		Page page = usersManager.findByPageRequest(pageRequest);
		savePage(page);
		return LIST_JSP;
	}
	
	/** 查看对象*/
	public String show() {
		
		return SHOW_JSP;
	}
	
	/** 进入新增页面
	 * @throws Exception */
	public String create() throws Exception {
		allRoles = rolesManager.findAll();
		checkedRoleIds = users.getRoleIds();
		return CREATE_JSP;
	}
	
	/** 保存新增对象 
	 * @throws Exception */
	public String save() throws Exception {
//		根据页面上的checkbox 整合User的Roles Set
		HibernateWebUtils.mergeByCheckedIds(users.getRoles(), checkedRoleIds, Roles.class);
		usersManager.save(users);
		return LIST_ACTION;
	}
	
	/**进入更新页面
	 * @throws Exception */
	public String edit() throws Exception {
		allRoles = rolesManager.findAll();
		allMenus = menusManager.findAll();
		checkedRoleIds = users.getRoleIds();
		checkedMenuIds = users.getMenuIds();
		return EDIT_JSP;
	}
	
	/**保存更新对象
	 * @throws Exception */
	public String update() throws Exception {
		HibernateWebUtils.mergeByCheckedIds(users.getRoles(), checkedRoleIds, Roles.class);
		HibernateWebUtils.mergeByCheckedIds(users.getMenus(), checkedMenuIds, Menus.class);
		usersManager.update(this.users);
		return LIST_ACTION;
	}
	
	/**删除对象*/
	public String delete() {
		for(int i = 0; i < items.length; i++) {
			Hashtable params = HttpUtils.parseQueryString(items[i]);
			java.lang.Long id = new java.lang.Long((String)params.get("id"));
			usersManager.removeById(id);
		}
		return LIST_ACTION;
	}

	public List<Long> getCheckedRoleIds() {
		return checkedRoleIds;
	}

	public void setCheckedRoleIds(List<Long> checkedRoleIds) {
		this.checkedRoleIds = checkedRoleIds;
	}
	public List<Roles> getAllRoles() {
		return allRoles;
	}

	public List<Menus> getAllMenus()
	{
		return allMenus;
	}

	public void setAllMenus(List<Menus> allMenus)
	{
		this.allMenus = allMenus;
	}

	public List<Long> getCheckedMenuIds()
	{
		return checkedMenuIds;
	}

	public void setCheckedMenuIds(List<Long> checkedMenuIds)
	{
		this.checkedMenuIds = checkedMenuIds;
	}

	public MenusManager getMenusManager()
	{
		return menusManager;
	}

	public void setMenusManager(MenusManager menusManager)
	{
		this.menusManager = menusManager;
	}



}
