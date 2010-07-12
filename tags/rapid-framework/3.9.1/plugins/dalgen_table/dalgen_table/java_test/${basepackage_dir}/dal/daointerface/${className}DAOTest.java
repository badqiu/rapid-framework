<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dal.daointerface;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

import cn.org.rapid_framework.test.context.TestMethodContext;
import ${basepackage}.dal.query.${className}Query;
import ${basepackage}.dal.dataobject.${className}DO;

import static junit.framework.Assert.*;

<#include "/java_imports.include">

public class ${className}DAOTest extends BaseDaoTestCase{
    
    private ${className}DAO dao;
    
    @Autowired
    public void set${className}DAO(${className}DAO dao) {
        this.dao = dao;
    }

    @Override 
    protected String[] getDbUnitDataFiles() {
        //通过 TestMethodContext.getMethodName() 可以得到当前正在运行的测试方法名称
        return new String[]{"classpath:testdata/common.xml","classpath:testdata/${className}.xml",
                            "classpath:testdata/${className}_"+TestMethodContext.getMethodName()+".xml"};
    }

    public void test_crud() {
        ${className}DO target = Factory.new${className}DO();
       
        dao.insert(target);
        
        dao.update(target);
        
        assertNotNull(dao.queryById(target.get${table.idColumn.columnName}()));
        dao.deleteById(target.get${table.idColumn.columnName}());
        
    }
    
    //数据库单元测试前会开始事务，结束时会回滚事务，所以测试方法可以不用关心测试数据的删除
    @Test
    public void findPage() {

        ${className}Query query = Factory.new${className}Query();
        PageList page = dao.findPage(query);
        
        assertEquals(pageNo,page.getPageNo());
        assertEquals(pageSize,page.getPageSize());
        assertNotNull(page);
        
    }
    
    static int pageNo = 1;
    static int pageSize = 10;   
    
    public static class Factory {
        public static ${className}Query new${className}Query() {
            ${className}Query query = new ${className}Query();
            query.setPageNo(pageNo);
            query.setPageSize(pageSize);
            query.setOrderBy(null);
            
            <#list table.columns as column>
                <#if column.isNotIdOrVersionField>
                <#if column.isDateTimeColumn && !column.contains("begin,start,end")>
            query.set${column.columnName}Begin(new ${column.simpleJavaType}(System.currentTimeMillis()));
            query.set${column.columnName}End(new ${column.simpleJavaType}(System.currentTimeMillis()));
                <#else>
            query.set${column.columnName}(new ${column.simpleJavaType}("1"));
                </#if>
                </#if>
            </#list>
            
            return query;
        }
        
        public static ${className}DO new${className}DO() {
            ${className}DO target = new ${className}DO();
            
            <#list table.columns as column>
                <#if column.isNotIdOrVersionField>
                    <#if column.isDateTimeColumn>
            target.set${column.columnName}(new ${column.javaType}(System.currentTimeMillis()));
                    <#else>
            target.set${column.columnName}(new ${column.javaType}("${column.testData}"));
                    </#if>
                </#if>
            </#list>
            
            return target;
        }
    }
}
