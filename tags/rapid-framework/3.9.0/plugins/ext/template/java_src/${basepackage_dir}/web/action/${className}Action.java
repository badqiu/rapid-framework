<#include "custom.include">
<#include "java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.web.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.*;

<#include "java_imports.include">

public class ${className}Action extends BaseStrutsAction {

	private ${className}Manager ${classNameLower}Manager;
	
	/** 
	 * 通过spring自动注入
	 **/
	public void set${className}Manager(${className}Manager manager) {
		this.${classNameLower}Manager = manager;
	}
	
	public ActionForward json(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			  HttpServletResponse response) {
		int DEFAULT_PAGE_SIZE = 10;
		
		String strStart = request.getParameter("start");
		String strLimit = request.getParameter("limit");
		
		int start = 0;
		int limit = DEFAULT_PAGE_SIZE;
		if(StringUtils.isNotBlank(strStart))
			start = Integer.valueOf(strStart);
		if(StringUtils.isNotBlank(strLimit))
			limit = Integer.valueOf(strLimit);

		Page page = ${classNameLower}Manager.findByHql(start/limit+1, limit);
		List<${className}> ${classNameLower}List = (List)page.getResult();
		
		ListRange<${className}> resultList = new ListRange<${className}>();
		resultList.setList(${classNameLower}List);
		resultList.setTotalSize(page.getTotalNumberOfElements());
		resultList.setMessage("ok");
		
		String json = JSONObject.fromObject(resultList).toString();
		
		try {
			response.setContentType("text/javascript;charset=UTF-8");
			response.getWriter().write(json);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	<#macro generateIdParameter>
		<#if table.compositeId>
			${className}Id id = (${className}Id)copyProperties(${className}Id.class,form);
		<#else>
			<#list table.compositeIdColumns as column>
			${column.javaType} id = new ${column.javaType}(request.getParameter("${column.columnNameLower}"));
			</#list>
		</#if>
	</#macro>