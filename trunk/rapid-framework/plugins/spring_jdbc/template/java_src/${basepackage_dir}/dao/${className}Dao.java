<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import static cn.org.rapid_framework.util.ObjectUtils.*;

<#include "/java_imports.include">
@Repository
public class ${className}Dao extends BaseSpringJdbcDao<${className},${table.idColumn.javaType}>{
	
	//注意: getSqlGenerator()可以生成基本的：增删改查sql语句, 通过这个父类已经封装了增删改查操作
    // sqlgenerator参考: http://code.google.com/p/rapid-framework/wiki/rapid_sqlgenerator
    
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
	
	public Page findPage(${className}Query query) {
		//XsqlBuilder syntax,please see http://code.google.com/p/rapid-xsqlbuilder
		// [column]为字符串拼接, {column}为使用占位符. [column]为使用字符串拼接,如username='[username]',偷懒时可以使用字符串拼接 
		// [column] 为PageRequest的属性
		String sql = "select "+ getSqlGenerator().getColumnsSql("t") + " from ${table.sqlName} t where 1=1 "
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
		if(isNotEmpty(query.get${column.columnName}())) {
		    sql2.append(" and t.${column.sqlName} >= :${column.columnNameLower}Begin ");
		}
		if(isNotEmpty(query.get${column.columnName}())) {
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
		String sql = "select " + getSqlGenerator().getColumnsSql() + " from ${table.sqlName} where ${column.columnNameLower}=?";
		return (${className})getSimpleJdbcTemplate().queryForObject(sql, ParameterizedBeanPropertyRowMapper.newInstance(getEntityClass()), v);
	}	
	
	</#if>
	</#list>

}
