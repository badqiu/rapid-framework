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

import com.awd.dao.ResourcesDao;
import com.awd.model.Authorities;
import com.awd.model.Resources;
import com.awd.model.Roles;
import com.awd.service.AuthoritiesManager;
import com.awd.service.RolesManager;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * @author badqiu email:badqiu(a)gmail.com
 * @version 1.0
 * @since 1.0
 */


public class RolesAction extends BaseStruts2Action implements Preparable,ModelDriven{
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	//forward paths
	protected static final String QUERY_JSP = "/pages/Roles/query.jsp";
	protected static final String LIST_JSP= "/pages/Roles/list.jsp";
	protected static final String CREATE_JSP = "/pages/Roles/create.jsp";
	protected static final String EDIT_JSP = "/pages/Roles/edit.jsp";
	protected static final String SHOW_JSP = "/pages/Roles/show.jsp";
	//redirect paths,startWith: !
	protected static final String LIST_ACTION = "!/pages/Roles/list.do";
	
	private RolesManager rolesManager;
	private AuthoritiesManager authoritiesManager;
	
	private Roles roles;
	java.lang.Long id = null;
	private String[] items;

	// 权限相关属�??
	private List<Authorities> allAuths; //全部可�?�权限列�?
	private List<Long> checkedAuthIds;//页面中钩选的权限id列表
	
	public void prepare() throws Exception {
		if (isNullOrEmptyString(id)) {
			roles = new Roles();
		} else {
			roles = (Roles)rolesManager.getById(id);
		}
	}
	
	/** 通过spring自动注入 */
	public void setRolesManager(RolesManager manager) {
		this.rolesManager = manager;
	}	
	

	public Object getModel() {
		return roles;
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
		Page page = rolesManager.findByPageRequest(pageRequest);
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
		allAuths = authoritiesManager.findAll();
		checkedAuthIds = roles.getAuthIds();
		return CREATE_JSP;
	}
	
	/** 保存新增对象 
	 * @throws Exception */
	public String save() throws Exception {
		//根据页面上的checkbox 整合Role的Authorities Set.
		HibernateWebUtils.mergeByCheckedIds(roles.getAuthorities(), checkedAuthIds, Authorities.class);

		rolesManager.save(roles);
		return LIST_ACTION;
	}
	
	/**进入更新页面
	 * @throws Exception */
	public String edit() throws Exception {
		allAuths = authoritiesManager.findAll();
		checkedAuthIds = roles.getAuthIds();
		return EDIT_JSP;
	}
	
	/**保存更新对象
	 * @throws Exception */
	public String update() throws Exception {
		//根据页面上的checkbox 整合Role的Authorities Set.
		HibernateWebUtils.mergeByCheckedIds(roles.getAuthorities(), checkedAuthIds, Authorities.class);

		rolesManager.update(this.roles);
		return LIST_ACTION;
	}
	
	/**删除对象*/
	public String delete() {
		for(int i = 0; i < items.length; i++) {
			Hashtable params = HttpUtils.parseQueryString(items[i]);
			java.lang.Long id = new java.lang.Long((String)params.get("id"));
			rolesManager.removeById(id);
		}
		return LIST_ACTION;
	}

	public void setAuthoritiesManager(AuthoritiesManager authoritiesManager)
	{
		this.authoritiesManager = authoritiesManager;
	}

	public List<Authorities> getAllAuths()
	{
		return allAuths;
	}

	public void setAllAuths(List<Authorities> allAuths)
	{
		this.allAuths = allAuths;
	}

	public List<Long> getCheckedAuthIds()
	{
		return checkedAuthIds;
	}

	public void setCheckedAuthIds(List<Long> checkedAuthIds)
	{
		this.checkedAuthIds = checkedAuthIds;
	}





}
