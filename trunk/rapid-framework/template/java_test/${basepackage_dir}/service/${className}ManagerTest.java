<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import static junit.framework.Assert.*;

<#include "/java_imports.include">

public class ${className}ManagerTest extends BaseManagerTestCase{

	private ${className}Manager manager;
	
	@Autowired
	public void set${className}Manager(${className}Manager manager) {
		this.manager = manager;
	}

	@Override
	protected String[] getDbUnitDataFiles() {
		return new String[]{"classpath:common_testdata.xml","classpath:${className}_testdata.xml"};
	}

	@Test
	public void crud() {
		${className} obj = new ${className}();
		
		<#list table.columns as column>
	  		<#if column.isNotIdOrVersionField>
	  			<#if column.isDateTimeColumn>
	  	obj.set${column.columnName}(new ${column.javaType}(System.currentTimeMillis()));
	  			<#else>
	  	obj.set${column.columnName}(new ${column.javaType}("1"));
	  			</#if>
			</#if>
		</#list>
		
		manager.save(obj);
		manager.getEntityDao().flush();
		
		manager.update(obj);
		manager.getEntityDao().flush();
		
	<#if table.compositeId>
		assertNotNull(manager.getById(obj.getId()));
		
		manager.removeById(obj.getId());
		manager.getEntityDao().flush();
	<#else>
		assertNotNull(obj.get${table.idColumn.columnName}());
		
		manager.removeById(obj.get${table.idColumn.columnName}());
		manager.getEntityDao().flush();
	</#if>
	
	}
}
