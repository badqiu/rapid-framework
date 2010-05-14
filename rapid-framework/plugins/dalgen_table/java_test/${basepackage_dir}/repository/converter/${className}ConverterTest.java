<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import static junit.framework.Assert.*;

<#include "/java_imports.include">

public class ${className}RepositoryTest extends BaseLocalRepositoryTestCase{
	
	private ${className}Repository dao;
	
	@Override
	protected String[] getDbUnitDataFiles() {
		return new String[]{"classpath:common_testdata.xml","classpath:${className}_testdata.xml"};
	}
	
	public void test_findPage() {
	    
	}
	
    public void test_crud() {
        ${className} target = new ${className}();
        
        <#list table.columns as column>
            <#if column.isNotIdOrVersionField>
                <#if column.isDateTimeColumn>
        target.set${column.columnName}(new ${column.javaType}(System.currentTimeMillis()));
                <#else>
        target.set${column.columnName}(new ${column.javaType}("${column.testData}"));
                </#if>
            </#if>
        </#list>
        
        dao.create(target);
        
        dao.update(target);
        
        assertNotNull(dao.queryById(target.get${table.idColumn.columnNameLower}()));
        dao.removeById(target.getId());
        
    }
	
}
