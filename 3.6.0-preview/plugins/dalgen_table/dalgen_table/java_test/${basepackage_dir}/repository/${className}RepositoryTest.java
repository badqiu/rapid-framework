<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import junit.framework.TestCase;
import static junit.framework.Assert.*;

<#include "/java_imports.include">

public class ${className}RepositoryTest extends TestCase{
	
	private ${className}Repository dao;
	
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
        
        dao.create${className}(target);
        
        dao.update${className}(target);
        
        assertNotNull(dao.queryById(target.get${table.idColumn.columnName}()));
        dao.removeById(target.getId());
        
    }
	
}
