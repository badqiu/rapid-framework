<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.flex.facade;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

<#include "/java_imports.include">

/**
 * 带有@Service和@@RemotingDestination注解bean则会被Spring管理并自动导出为BlazeDS的RemoteObject
 * @author badqiu
 * @author hunhun 
 */
@Service
@RemotingDestination
public class ${className}Facade {

	private ${className}Manager ${classNameLower}Manager;
	/**通过spring注入${className}Manager*/
	public void set${className}Manager(${className}Manager ${classNameLower}Manager) {
		this.${classNameLower}Manager = ${classNameLower}Manager;
	}
	
	/** 通过PageQuery查询列表 */
	@RemotingInclude
	public Page<${className}> list(${className}Query  query) {	
		return ${classNameLower}Manager.findPage(query);	
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
