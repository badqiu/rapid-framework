<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.flex.service;

import org.springframework.stereotype.Component;

import cn.org.rapid_framework.util.HibernateBeanSerializer;

<#include "/java_imports.include">

/**
 * 名称以FlexService结尾并且装载进Spring则会自动导出为BlazeDS的RemoteObject对象
 */
@Component
public class ${className}FlexService extends BaseRemoteFlexService<${className}>{

	private ${className}Manager ${classNameLower}Manager;
	/**通过spring注入${className}Manager*/
	public void set${className}Manager(${className}Manager ${classNameLower}Manager) {
		this.${classNameLower}Manager = ${classNameLower}Manager;
	}
	
	/**通过PageRequest查询列表*/
	public Page list(PageRequest pr) {
		${className}Query query = newQuery(${className}Query.class,pr);
		Page page = ${classNameLower}Manager.findPage(query);
		return page;
	}
	
	public  ${className} save(${className} vo) {
		${classNameLower}Manager.saveOrUpdate(vo);
		return vo;
	}
	
	public void del(${table.idColumn.javaType}[] ids) {
		for(${table.idColumn.javaType} id : ids) {
			${classNameLower}Manager.removeById(id);
		}
	}

	public ${className} getById(${table.idColumn.javaType} id) {
		return ${classNameLower}Manager.getById(id);
	}
}
