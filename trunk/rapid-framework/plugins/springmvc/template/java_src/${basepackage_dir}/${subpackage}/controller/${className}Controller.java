<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.${subpackage}.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

<#include "/java_imports.include">

@Controller
public class ${className}Controller extends BaseSpringController{
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	private ${className}Manager ${classNameLower}Manager;
	
	private final String LIST_ACTION = "redirect:/${subpackage}/${className}/list.do";
	
	public ${className}Controller() {
	}
	
	/** 
	 * 通过spring自动注入
	 **/
	public void set${className}Manager(${className}Manager manager) {
		this.${classNameLower}Manager = manager;
	}
	
	/** 
	 * 进入查询页面
	 **/
	public ModelAndView query(HttpServletRequest request,HttpServletResponse response) {
		return new ModelAndView("${jspFileBasePath}/query");
	}
	
	/** 
	 * 执行搜索 
	 **/
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) {
		PageRequest pageRequest = newPageRequest(request,DEFAULT_SORT_COLUMNS);
		Page page = this.${classNameLower}Manager.findByPageRequest(pageRequest);
		savePage(page, request);
		return new ModelAndView("${jspFileBasePath}/list");
	}
	
	/** 
	 * 查看对象
	 **/
	public ModelAndView show(HttpServletRequest request,HttpServletResponse response) throws Exception {
		<@generateIdParameter/>
		${className} ${classNameLower} = (${className})${classNameLower}Manager.getById(id);
		return new ModelAndView("${jspFileBasePath}/show","${classNameLower}",${classNameLower});
	}
	
	/** 
	 * 进入新增页面
	 **/
	public ModelAndView create(HttpServletRequest request,HttpServletResponse response,${className} ${classNameLower}) throws Exception {
		return new ModelAndView("${jspFileBasePath}/create","${classNameLower}",${classNameLower});
	}
	
	/** 
	 * 保存新增对象
	 **/
	public ModelAndView save(HttpServletRequest request,HttpServletResponse response,${className} ${classNameLower}) throws Exception {
		${classNameLower}Manager.save(${classNameLower});
		return new ModelAndView(LIST_ACTION);
	}
	
	/**
	 * 进入更新页面
	 **/
	public ModelAndView edit(HttpServletRequest request,HttpServletResponse response) throws Exception {
		<@generateIdParameter/>
		${className} ${classNameLower} = (${className})${classNameLower}Manager.getById(id);
		return new ModelAndView("${jspFileBasePath}/edit","${classNameLower}",${classNameLower});
	}
	
	/**
	 * 保存更新对象
	 **/
	public ModelAndView update(HttpServletRequest request,HttpServletResponse response) throws Exception {
		<@generateIdParameter/>
		
		${className} ${classNameLower} = (${className})${classNameLower}Manager.getById(id);
		bind(request,${classNameLower});
		${classNameLower}Manager.update(${classNameLower});
		return new ModelAndView(LIST_ACTION);
	}
	
	/**
	 *删除对象
	 **/
	public ModelAndView delete(HttpServletRequest request,HttpServletResponse response) {
		String[] items = request.getParameterValues("items");
		for(int i = 0; i < items.length; i++) {
			Hashtable params = HttpUtils.parseQueryString(items[i]);
			
			<#if table.compositeId>
			${className}Id id = (${className}Id)copyProperties(${className}Id.class,params);
			<#else>
				<#list table.compositeIdColumns as column>
			${column.javaType} id = new ${column.javaType}((String)params.get("${column.columnNameLower}"));
				</#list>
			</#if>
			
			${classNameLower}Manager.removeById(id);
		}
		return new ModelAndView(LIST_ACTION);
	}
	
}

<#macro generateIdParameter>
	<#if table.compositeId>
		${className}Id id = new ${className}Id();
		bind(request, id);
	<#else>
		<#list table.compositeIdColumns as column>
		${column.javaType} id = new ${column.javaType}(request.getParameter("${column.columnNameLower}"));
		</#list>
	</#if>
</#macro>