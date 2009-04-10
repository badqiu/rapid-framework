<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.${subpackage}.dao;

<#include "/java_imports.include">

import java.io.Serializable;
import java.util.List;

import javax.xml.registry.infomodel.User;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Component;

@Component
public class ${className}Dao extends BaseSpringJdbcDao{

	public Class getEntityClass() {
		return ${className}.class;
	}
	public String getIdentifierPropertyName() {
		<#if table.singleId>
		return "${table.idColumn.columnNameLower}";
		<#else>
		return null;
		</#if>
	}
	
	public void deleteById(Serializable id) {
		getSimpleJdbcTemplate().update("delete from ${table.sqlName} where ${table.idColumn.sqlName}=?", id);
	}
	
	public List findAll() {
		String sql = "select <#list table.columns as column>${column.sqlName}<#if column_has_next>,</#if></#list> from ${table.sqlName}";
		return getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(getEntityClass()));
	}

	public ${className} getById(Serializable id) {
		String sql = "select <#list table.columns as column>${column.sqlName}<#if column_has_next>,</#if></#list> from ${table.sqlName} where ${table.idColumn.sqlName}=? ";
		return getSimpleJdbcTemplate().queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(${className}.class), id);
	}

	public void save(Object entity) {
		String sql = "insert into ${table.sqlName} " 
			 + " (<#list table.columns as column>${column.sqlName}<#if column_has_next>,</#if></#list>) " 
			 + " values(<#list table.columns as column>:${column.columnNameLower}<#if column_has_next>,</#if></#list>)";
		insertWithIdentity(entity,sql);
	}
	
	public void update(Object entity) {
		String sql = "update ${table.sqlName} set <#list table.columns as column>${column.sqlName}=:${column.columnNameLower}<#if column_has_next>,</#if></#list> "
					+ " where ${table.idColumn.sqlName}=:${table.idColumn.columnNameLower}";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}
	
	public Page findByPageRequest(PageRequest pageRequest) {
		//XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
		String sql = "select <#list table.columns as column>${column.sqlName}<#if column_has_next>,</#if></#list> from ${table.sqlName} as a where 1=1 "
			<#list table.columns as column>
			  	<#if column.isNotIdOrVersionField>
				+ "/~ and a.${column.sqlName} = '[${column.columnNameLower}]' ~/"
				</#if>
			</#list>
				+ "/~ order by [sortColumns] ~/";
		return pageQuery(sql,pageRequest);
	}
	
	public boolean isUnique(Object entity, String uniquePropertyNames) {
		return false;
	}
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		String sql = "select <#list table.columns as column>${column.sqlName}<#if column_has_next>,</#if></#list> from ${table.sqlName} where ${column.columnNameLower}=?";
		return (${className})getSimpleJdbcTemplate().queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(User.class), v);
	}	
	</#if>
	</#list>

}
