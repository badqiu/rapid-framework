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

import javacommon.util.extjs.ExtJsPageHelper;
import javacommon.util.extjs.ListRange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import cn.org.rapid_framework.generator.provider.java.model.JavaClass;
import cn.org.rapid_framework.generator.provider.java.model.JavaField;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import ${basepackage}.model.${className};
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.ModelDriven;
import static javacommon.util.extjs.Struts2JsonHelper.*;

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
	protected static final String LIST_ACTION = "!${actionBasePath}/list.${actionExtension}";
	
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



	/**
	 * ExtGrid使用
	 * 列表
	 * @throws IOException
	 */
	public void extlist() throws IOException
	{
		PageRequest<Map> pr = ExtJsPageHelper.createPageRequestForExtJs(getRequest(), DEFAULT_SORT_COLUMNS);
		Page page = ${classNameLower}Manager.findPage(pr);
		
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
		catch (Exception e)
		{
			result.put("failure", true);
			result.put("msg", e.getMessage());
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
		catch (Exception e)
		{
			result.put("failure", true);
			result.put("msg", e.getMessage());
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
				<#list table.compositeIdColumns as column>
				${column.javaType} id = new ${column.javaType}((String)idarray[i]);
					</#list>
				${classNameLower}Manager.removeById(id);
			}
			result.put("success", true);
			result.put("msg", "删除成功");
		}
		catch (Exception e)
		{
			result.put("failure", true);
			result.put("msg", e.getMessage());
			e.printStackTrace();
		}
		outJson(result);
	}
	
}
