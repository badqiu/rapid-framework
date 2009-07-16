/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */

package com.awd.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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


public class MenusAction extends BaseStruts2Action implements Preparable,ModelDriven{
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	//forward paths
	protected static final String QUERY_JSP = "/pages/Menus/query.jsp";
	protected static final String LIST_JSP= "/pages/Menus/list.jsp";
	protected static final String CREATE_JSP = "/pages/Menus/create.jsp";
	protected static final String EDIT_JSP = "/pages/Menus/edit.jsp";
	protected static final String SHOW_JSP = "/pages/Menus/show.jsp";
	//redirect paths,startWith: !
	protected static final String LIST_ACTION = "!/pages/Menus/list.do";
	
	private MenusManager menusManager;
	
	private Menus menus;
	java.lang.Long id = null;
	private String[] items;

	public void prepare() throws Exception {
		if (isNullOrEmptyString(id)) {
			menus = new Menus();
		} else {
			menus = (Menus)menusManager.getById(id);
		}
	}
	
	/** 通过spring自动注入 */
	public void setMenusManager(MenusManager manager) {
		this.menusManager = manager;
	}	
	
	public Object getModel() {
		return menus;
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
		Page page = menusManager.findByPageRequest(pageRequest);
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
		menusManager.save(menus);
		return LIST_ACTION;
	}
	
	/**进入更新页面*/
	public String edit() {
		return EDIT_JSP;
	}
	
	/**保存更新对象*/
	public String update() {
		menusManager.update(this.menus);
		return LIST_ACTION;
	}
	
	/**删除对象*/
	public String delete() {
		for(int i = 0; i < items.length; i++) {
			Hashtable params = HttpUtils.parseQueryString(items[i]);
			java.lang.Long id = new java.lang.Long((String)params.get("id"));
			menusManager.removeById(id);
		}
		return LIST_ACTION;
	}

}
