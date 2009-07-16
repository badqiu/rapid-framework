<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<#assign actionExtension = "do">
package ${basepackage}.action;

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

import com.awd.model.${className};
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.ModelDriven;

<#include "/java_imports.include">

public class ${className}Action extends BaseStruts2Action implements Preparable,ModelDriven{
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	//forward paths
	protected static final String QUERY_JSP = "${jspFileBasePath}/query.jsp";
	protected static final String LIST_JSP= "${jspFileBasePath}/list.jsp";
	protected static final String CREATE_JSP = "${jspFileBasePath}/create.jsp";
	protected static final String EDIT_JSP = "${jspFileBasePath}/edit.jsp";
	protected static final String SHOW_JSP = "${jspFileBasePath}/show.jsp";
	//redirect paths,startWith: !
	protected static final String LIST_ACTION = "!${strutsActionBasePath}/list.${actionExtension}";
	
	private ${className}Manager ${classNameLower}Manager;
	
	private ${className} ${classNameLower};
	<#list table.compositeIdColumns as column>
	${column.javaType} id = null;
	</#list>
	private String[] items;

	public void prepare() throws Exception {
		if (isNullOrEmptyString(id)) {
			${classNameLower} = new ${className}();
		} else {
			${classNameLower} = (${className})${classNameLower}Manager.getById(id);
		}
	}
	
	/** 通过spring自动注入 */
	public void set${className}Manager(${className}Manager manager) {
		this.${classNameLower}Manager = manager;
	}	
	
	public Object getModel() {
		return ${classNameLower};
	}
	
	<#list table.compositeIdColumns as column>
	public void set${column.columnName}(${column.javaType} val) {
		this.id = val;
	}
	</#list>	

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
		Page page = ${classNameLower}Manager.findByPageRequest(pageRequest);
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
		${classNameLower}Manager.save(${classNameLower});
		return LIST_ACTION;
	}
	
	/**进入更新页面*/
	public String edit() {
		return EDIT_JSP;
	}
	
	/**保存更新对象*/
	public String update() {
		${classNameLower}Manager.update(this.${classNameLower});
		return LIST_ACTION;
	}
	
	/**删除对象*/
	public String delete() {
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
		Page page = ${classNameLower}Manager.findByPageRequest(result);
		
		List<${className}> ${className}list = (List) page.getResult();
		ListRange<${className}> resultList = new ListRange<${className}>();
		resultList.setList(${className}list);
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
			${classNameLower}Manager.save(${classNameLower});
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
			${classNameLower}Manager.save(${classNameLower});
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
				${classNameLower}Manager.removeById(id);
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
		Class c = ${className}.class;
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
