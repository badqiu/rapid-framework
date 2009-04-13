<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.${subpackage}.dao;

import java.util.List;

<#include "/java_imports.include">

public class ${className}DaoTest extends BaseDaoTestCase{
	
	private ${className}Dao dao;
	/**通过spring注入${className}Dao*/
	public void set${className}Dao(${className}Dao dao) {
		this.dao = dao;
	}
	
	@Override
	protected void onTearDownInTransaction() throws Exception {
		super.onTearDownInTransaction();
	}
	
	@Override
	protected String[] getDbUnitDataFiles() {
		return new String[]{"classpath:common_testdata.xml","classpath:${className}_testdata.xml"};
	}
	
	public void testFindByPageRequest() {
		int pageNumber = 1;
		int pageSize = 10;
		
		PageRequest pageRequest = new PageRequest();
		pageRequest.setPageNumber(pageNumber);
		pageRequest.setPageSize(pageSize);
		pageRequest.setSortColumns(null);
		
		<#list table.columns as column>
	  		<#if column.isNotIdOrVersionField>
		pageRequest.getFilters().put("${column.columnNameLower}", "1");
			</#if>
		</#list>
		
		Page page = dao.findByPageRequest(pageRequest);
		
		assertEquals(pageNumber,page.getThisPageNumber());
		assertEquals(pageSize,page.getPageSize());
		List resultList = (List)page.getResult();
		assertNotNull(resultList);
		
	}
	
}
