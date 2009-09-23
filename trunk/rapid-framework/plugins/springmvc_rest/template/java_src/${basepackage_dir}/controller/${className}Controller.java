<#include "/custom.include">
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>   
<#assign classNameLowerCase = className?lower_case>   
<#assign pkJavaType = table.idColumn.javaType>   

package ${basepackage}.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

<#include "/java_imports.include">
@Controller
@RequestMapping("/${classNameLowerCase}")
public class ${className}Controller extends BaseRestSpringController<${className},${pkJavaType}>{
	//默认多列排序,example: username desc,createTime asc
	protected static final String DEFAULT_SORT_COLUMNS = null; 
	
	private ${className}Manager ${classNameFirstLower}Manager;
	
	private final String LIST_ACTION = "redirect:/${classNameLowerCase}";
		
	/** 
	 * 增加setXXXX()方法,spring就可以通过autowire自动设置对象属性
	 **/
	public void set${className}Manager(${className}Manager manager) {
		this.${classNameFirstLower}Manager = manager;
	}
	
	/** 列表 */
	@Override
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response,${className} ${classNameFirstLower}) {
		PageRequest<Map> pageRequest = newPageRequest(request,DEFAULT_SORT_COLUMNS);
		//pageRequest.getFilters(); //add custom filters
		
		Page page = this.${classNameFirstLower}Manager.findByPageRequest(pageRequest);
		
		ModelAndView result = toModelAndView(page, pageRequest);
		result.addObject("${classNameFirstLower}",${classNameFirstLower});
		return result;
	}
	
	/** 进入新增 */
	@Override
	public ModelAndView _new(HttpServletRequest request,HttpServletResponse response,${className} ${classNameFirstLower}) throws Exception {
		return new ModelAndView("/${classNameLowerCase}/new","${classNameFirstLower}",${classNameFirstLower});
	}
	
	/** 显示 */
	@Override
	public ModelAndView show(@PathVariable ${pkJavaType} id) throws Exception {
		${className} ${classNameFirstLower} = (${className})${classNameFirstLower}Manager.getById(id);
		return new ModelAndView("/${classNameLowerCase}/show","${classNameFirstLower}",${classNameFirstLower});
	}
	
	/** 编辑 */
	@Override
	public ModelAndView edit(@PathVariable ${pkJavaType} id) throws Exception {
		${className} ${classNameFirstLower} = (${className})${classNameFirstLower}Manager.getById(id);
		return new ModelAndView("/${classNameLowerCase}/edit","${classNameFirstLower}",${classNameFirstLower});
	}
	
	/** 保存新增 */
	@Override
	public ModelAndView create(HttpServletRequest request,HttpServletResponse response,${className} ${classNameFirstLower}) throws Exception {
		${classNameFirstLower}Manager.save(${classNameFirstLower});
		return new ModelAndView(LIST_ACTION);
	}
	
	/** 保存更新 */
	@Override
	public ModelAndView update(@PathVariable ${pkJavaType} id,HttpServletRequest request,HttpServletResponse response) throws Exception {
		${className} ${classNameFirstLower} = (${className})${classNameFirstLower}Manager.getById(id);
		bind(request,${classNameFirstLower});
		${classNameFirstLower}Manager.update(${classNameFirstLower});
		return new ModelAndView(LIST_ACTION);
	}
	
	/** 删除 */
	@Override
	public ModelAndView delete(@PathVariable ${pkJavaType} id) {
		${classNameFirstLower}Manager.removeById(id);
		return new ModelAndView(LIST_ACTION);
	}

	/** 批量删除 */
	@Override
	public ModelAndView batchDelete(${pkJavaType}[] items) {
		for(int i = 0; i < items.length; i++) {
			${classNameFirstLower}Manager.removeById(items[i]);
		}
		return new ModelAndView(LIST_ACTION);
	}
	
}

