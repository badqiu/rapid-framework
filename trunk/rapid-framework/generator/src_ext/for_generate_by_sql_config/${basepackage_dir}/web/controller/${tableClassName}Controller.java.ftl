<#assign className = tableConfig.table.className>   
<#assign classNameFirstLower = className?uncap_first>   
<#assign classNameLowerCase = className?lower_case>   
<#assign pkJavaType = tableConfig.table.idColumn.javaType>   

package ${basepackage}.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;
import cn.org.rapid_framework.web.scope.Flash;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;



@Controller
@RequestMapping("/${classNameLowerCase}")
public class ${className}Controller {
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	private ${className}Manager ${classNameFirstLower}Manager;
	
	private final String LIST_ACTION = "redirect:/${classNameLowerCase}";
		
	/** 
	 * 增加setXXXX()方法,spring就可以通过autowire自动设置对象属性,注意大小写
	 **/
	public void set${className}Manager(${className}Manager manager) {
		this.${classNameFirstLower}Manager = manager;
	}

	/**
	 * 增加了@ModelAttribute的方法可以在本controller的方法调用前执行,可以存放一些共享变量,如枚举值
	 */
	@ModelAttribute
	public void init(ModelMap model) {
		model.put("now", new java.sql.Timestamp(System.currentTimeMillis()));
	}
	
	/** 列表 */
	@RequestMapping
	public String index(ModelMap model,${className}Query query,HttpServletRequest request,HttpServletResponse response) {
		Page page = this.${classNameFirstLower}Manager.findPage(query);
		
		model.addAllAttributes(toModelMap(page, query));
		return "/${className?lower_case}/index";
	}
	
	/** 进入新增 */
	@RequestMapping(value="/new")
	public String _new(ModelMap model,${className} ${classNameFirstLower},HttpServletRequest request,HttpServletResponse response) throws Exception {
		model.addAttribute("${classNameFirstLower}",${classNameFirstLower});
		return "/${classNameLowerCase}/new";
	}
	
	/** 显示 */
	@RequestMapping(value="/{id}")
	public String show(ModelMap model,@PathVariable ${pkJavaType} id) throws Exception {
		${className} ${classNameFirstLower} = (${className})${classNameFirstLower}Manager.getById(id);
		model.addAttribute("${classNameFirstLower}",${classNameFirstLower});
		return "/${classNameLowerCase}/show";
	}
	
	/** 编辑 */
	@RequestMapping(value="/{id}/edit")
	public String edit(ModelMap model,@PathVariable ${pkJavaType} id) throws Exception {
		${className} ${classNameFirstLower} = (${className})${classNameFirstLower}Manager.getById(id);
		model.addAttribute("${classNameFirstLower}",${classNameFirstLower});
		return "/${classNameLowerCase}/edit";
	}
	
	/** 保存新增 */
	@RequestMapping(method=RequestMethod.POST)
	public String create(ModelMap model,${className} ${classNameFirstLower},HttpServletRequest request,HttpServletResponse response) throws Exception {
		${classNameFirstLower}Manager.save(${classNameFirstLower});
		Flash.current().success("创建成功"); //存放在Flash中的数据,在下一次http请求中仍然可以读取数据,error()用于显示错误消息
		return LIST_ACTION;
	}
	
	/** 保存更新 */
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public String update(ModelMap model,@PathVariable ${pkJavaType} id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		${className} ${classNameFirstLower} = (${className})${classNameFirstLower}Manager.getById(id);
		bind(request,${classNameFirstLower});
		${classNameFirstLower}Manager.update(${classNameFirstLower});
		Flash.current().success("更新成功");
		return LIST_ACTION;
	}
	
	/** 删除 */
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public String delete(ModelMap model,@PathVariable ${pkJavaType} id) {
		${classNameFirstLower}Manager.removeById(id);
		Flash.current().success("删除成功");
		return LIST_ACTION;
	}

	/** 批量删除 */
	@RequestMapping(method=RequestMethod.DELETE)
	public String batchDelete(ModelMap model,@RequestParam("items") ${pkJavaType}[] items) {
		for(int i = 0; i < items.length; i++) {
			${classNameFirstLower}Manager.removeById(items[i]);
		}
		Flash.current().success("删除成功");
		return LIST_ACTION;
	}
	
	<#list tableConfig.sqls as sql>
	/**
	 * ${sql.remarks!}
	 */
	<#if (sql.params?size > params2paramObjectLimit) >
	@RequestMapping(value="/${sql.operation}",method=<@getRequestMethod sql/>)
	public <@generateResultClassName sql/> ${sql.operation}(ModelMap model,${sql.parameterClassName} param) throws DataAccessException {
		Result result = ${className}Facade.${sql.operation}(param);
		model.addAttribute();
	}
	</#if>
	</#list>
	
}

<#macro getRequestMethod sql>
<#if sql.updateSql>
RequestMethod.PUT
</#if>
<#if sql.selectSql>
RequestMethod.GET
</#if>
<#if sql.deleteSql>
RequestMethod.DELETE
</#if>
<#if sql.insertSql>
RequestMethod.POST
</#if>
</#macro>
