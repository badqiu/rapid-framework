<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

<#include "/java_imports.include">
@Repository
public class ${className}Dao extends BaseSpringJdbcDao<${className},${table.idColumn.javaType}>{
	
	//注意: getSqlGenerator()可以生成基本的：增删改查sql语句, 通过这个父类已经封装了增删改查操作
	public Class getEntityClass() {
		return ${className}.class;
	}
	
	public void save(${className} entity) {
		String sql = getSqlGenerator().getInsertSql();
		insertWithIdentity(entity,sql); //for sqlserver and mysql
		
		//其它主键生成策略
		//insertWithOracleSequence(entity,"sequenceName",sql); //oracle sequence: 
		//insertWithDB2Sequence(entity,"sequenceName",sql); //db2 sequence:
		//insertWithUUID(entity,sql); //uuid
		//insertWithAssigned(entity,sql); //手工分配
	}
	
	public Page findPage(PageRequest<Map> pageRequest) {
		//XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
		// [column]为字符串拼接, {column}为使用占位符. 以下为图方便采用sql拼接,适用性能要求不高的应用,使用占位符方式可以优化性能. 
		// [column] 为PageRequest.getFilters()中的key
		String sql = "select "+ getSqlGenerator().getColumnsSql("t") + " from ${table.sqlName} t where 1=1 "
			<#list table.columns as column>
			  	<#if column.isNotIdOrVersionField>
			  	<#if column.isDateTimeColumn>
				+ "/~ and t.${column.sqlName} >= '[${column.columnNameLower}Begin]' ~/"
				+ "/~ and t.${column.sqlName} <= '[${column.columnNameLower}End]' ~/"
				<#else>
			  	+ "/~ and t.${column.sqlName} = '[${column.columnNameLower}]' ~/"
			  	</#if>
				</#if>
			</#list>
				+ "/~ order by [sortColumns] ~/";
		return pageQuery(sql,pageRequest);
	}
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		String sql = "select " + getSqlGenerator().getColumnsSql() + " from ${table.sqlName} where ${column.columnNameLower}=?";
		return (${className})getSimpleJdbcTemplate().queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(getEntityClass()), v);
	}	
	
	</#if>
	</#list>

}
