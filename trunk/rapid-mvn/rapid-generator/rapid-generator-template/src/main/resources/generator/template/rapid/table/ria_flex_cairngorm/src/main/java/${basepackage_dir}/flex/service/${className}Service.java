<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.flex.service;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

<#include "/java_imports.include">

/**
 * @author badqiu,hunhun 
 * 带有@Service和@@RemotingDestination注解并且装载进Spring的bean则会自动导出为BlazeDS的RemoteObject对象
 */
@Service
@RemotingDestination
public class ${className}Service extends BaseRemoteFlexService<${className}>{

	private ${className}Manager ${classNameLower}Manager;
	/**通过spring注入${className}Manager*/
	public void set${className}Manager(${className}Manager ${classNameLower}Manager) {
		this.${classNameLower}Manager = ${classNameLower}Manager;
	}
	
	/**通过PageRequest查询列表*/
	@RemotingInclude
	public Page list(PageRequest pr) {
		${className}Query query = newQuery(${className}Query.class,pr);
		Page page = ${classNameLower}Manager.findPage(query);
		return page;
	}
	
	@RemotingInclude
	public  ${className} save(${className} vo) {
		${classNameLower}Manager.saveOrUpdate(vo);
		return vo;
	}
	
	@RemotingInclude
	public void del(${table.idColumn.javaType}[] ids) {
		for(${table.idColumn.javaType} id : ids) {
			${classNameLower}Manager.removeById(id);
		}
	}
	
	@RemotingInclude
	public ${className} getById(${table.idColumn.javaType} id) {
		return ${classNameLower}Manager.getById(id);
	}
}
