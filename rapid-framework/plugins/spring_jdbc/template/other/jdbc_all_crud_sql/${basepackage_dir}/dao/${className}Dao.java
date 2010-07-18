<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Component;
import static cn.org.rapid_framework.util.ObjectUtils.*;

<#include "/java_imports.include">
@Component
public class ${className}Dao extends BaseSpringJdbcDao<${className},${table.idColumn.javaType}>{
	
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
	
	public String getColumns() {
		return ""
				<#list table.columns as column>
				+" ${column.sqlName} as ${column.columnNameFirstLower}<#if column_has_next>,</#if>"
				</#list>
				+" from ${table.sqlName} ";
	}
	
	/**
	 * return sql for deleteById();
	 */
	public String getDeleteByIdSql() {
		return "delete from ${table.sqlName} where ${table.idColumn.sqlName}=?";
	}
	
	/**
	 * return sql for getById();
	 */
	public String getFindByIdSql() {
		return "select " + getColumns() + " where ${table.idColumn.sqlName}=? ";
	}
	
	public void save(${className} entity) {
		String sql = "insert into ${table.sqlName} " 
			 + " (<#list table.columns as column>${column.sqlName}<#if column_has_next>,</#if></#list>) " 
			 + " values "
			 + " (<#list table.columns as column>:${column.columnNameLower}<#if column_has_next>,</#if></#list>)";
		insertWithIdentity(entity,sql); //for sqlserver and mysql
		
		//其它主键生成策略
		//insertWithOracleSequence(entity,"sequenceName",sql); //oracle sequence: 
		//insertWithDB2Sequence(entity,"sequenceName",sql); //db2 sequence:
		//insertWithUUID(entity,sql); //uuid
		//insertWithAssigned(entity,sql); //手工分配
	}
	
	public void update(${className} entity) {
		String sql = "update ${table.sqlName} set "
					+ " <#list table.columns as column>${column.sqlName}=:${column.columnNameLower}<#if column_has_next>,</#if></#list> "
					+ " where ${table.idColumn.sqlName}=:${table.idColumn.columnNameLower}";
		getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(entity));
	}
	
	public List findAll() {
		String sql = "select " + getColumns() ;
		return getSimpleJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(getEntityClass()));
	}

	public Page findPage(${className}Query query) {
		//XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
		// [column]为字符串拼接, {column}为使用占位符. 如username='[username]',偷懒时可以使用字符串拼接 
		// [column] 为PageRequest的属性
		String sql = "select " + getColumns() + " t where 1=1 "
			<#list table.columns as column>
		  		<#if column.isNotIdOrVersionField>
		  		<#if column.isDateTimeColumn>
			+ "/~ and t.${column.sqlName} >= {${column.columnNameLower}Begin} ~/"
			+ "/~ and t.${column.sqlName} <= {${column.columnNameLower}End} ~/"
				<#else>
		  	+ "/~ and t.${column.sqlName} = {${column.columnNameLower}} ~/"
		  		</#if>
		  		</#if>
			</#list>
			+ "/~ order by [sortColumns] ~/";

		//生成sql2的原因是为了不喜欢使用xsqlbuilder的同学，请修改生成器模板，删除本段的生成
		StringBuilder sql2 = new StringBuilder("select "+ getSqlGenerator().getColumnsSql("t") + " from ${table.sqlName} t where 1=1 ");
		<#list table.columns as column>
		<#if column.isDateTimeColumn>
		if(isNotEmpty(query.get${column.columnName}Begin())) {
		    sql2.append(" and t.${column.sqlName} >= :${column.columnNameLower}Begin ");
		}
		if(isNotEmpty(query.get${column.columnName}End())) {
		    sql2.append(" and t.${column.sqlName} <= :${column.columnNameLower}End ");
		}
		<#else>
		if(isNotEmpty(query.get${column.columnName}())) {
		    sql2.append(" and t.${column.sqlName} = :${column.columnNameLower} ");
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
		String sql =  "select " + getColumns() + " where ${column.columnNameLower}=?";
		return (${className})DataAccessUtils.singleResult(getSimpleJdbcTemplate().queryForList(sql, ParameterizedBeanPropertyRowMapper.newInstance(getEntityClass()), v));
	}	
	</#if>
	</#list>

}
