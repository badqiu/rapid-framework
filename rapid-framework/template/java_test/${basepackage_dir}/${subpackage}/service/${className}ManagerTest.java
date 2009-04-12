<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${subpackage}.service;

import cn.org.rapid_framework.mock.MockOpenSessionInViewFilter;

<#include "/java_imports.include">

public class ${className}ManagerTest extends BaseManagerTestCase{

	private ${className}Manager manager;
	/**通过spring注入${className}Dao*/
	public void set${className}Manager(${className}Manager manager) {
		this.manager = manager;
	}

	@Override
	protected String[] getDbUnitDataFiles() {
		return new String[]{"classpath:common_testdata.xml","classpath:${className}_testdata.xml"};
	}

	@Override
	public void onTearDownInTransaction() throws Exception {
		super.onTearDownInTransaction();
	}
	
	public void testCrud() {
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
