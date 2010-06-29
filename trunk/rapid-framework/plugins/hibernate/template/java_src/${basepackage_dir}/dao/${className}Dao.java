<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

<#include "/java_imports.include">

import org.springframework.stereotype.Repository;

@Repository
public class ${className}Dao extends BaseHibernateDao<${className},${table.idColumn.javaType}>{

	public Class getEntityClass() {
		return ${className}.class;
	}
	
	public Page findPage(${className}Query query) {
        //XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
        // [column]为字符串拼接, {column}为使用占位符. [column]为使用字符串拼接,如username='[username]',偷懒时可以使用字符串拼接 
        // [column] 为PageRequest的属性
		String sql = "select t from ${className} t where 1=1 "
			<#list table.columns as column>
			  	<#if column.isNotIdOrVersionField>
			  	<#if column.isDateTimeColumn>
				+ "/~ and t.${column.columnNameLower} >= {${column.columnNameLower}Begin} ~/"
				+ "/~ and t.${column.columnNameLower} <= {${column.columnNameLower}End} ~/"
				<#else>
			  	+ "/~ and t.${column.columnNameLower} = {${column.columnNameLower}} ~/"
			  	</#if>
				</#if>
			</#list>
				+ "/~ order by [sortColumns] ~/";

        //生成sql2的原因是为了不喜欢使用xsqlbuilder的同学，请修改生成器模板，删除本段的生成
        StringBuilder sql2 = new StringBuilder("select t from ${className} t where 1=1 ");
        <#list table.columns as column>
        <#if column.isDateTimeColumn>
        if(isNotEmpty(query.get${column.columnName}Begin())) {
            sql2.append(" and  t.${column.columnNameLower} >= :${column.columnNameLower}Begin ");
        }
        if(isNotEmpty(query.get${column.columnName}End())) {
            sql2.append(" and  t.${column.columnNameLower} <= :${column.columnNameLower}End ");
        }
        <#else>
        if(isNotEmpty(query.get${column.columnName}())) {
            sql2.append(" and  t.${column.columnNameLower} = :${column.columnNameLower} ");
        }
        </#if>
        </#list>
        if(isNotEmpty(query.getSortColumns())) {
            sql2.append(" order by :sortColumns ");
        }	
        
		return pageQuery(sql,query);
	}
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return (${className}) findByProperty("${column.columnNameLower}",v);
	}	
	</#if>
	</#list>

}
