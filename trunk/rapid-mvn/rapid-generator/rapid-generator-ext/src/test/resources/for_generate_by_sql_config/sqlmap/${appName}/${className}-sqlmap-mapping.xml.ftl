<?xml version="1.0" encoding="GBK"?>
<!DOCTYPE sqlMap PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN" 
    "http://ibatis.apache.org/dtd/sql-map-2.dtd">
    
<#macro namespace>${tableConfig.className}.</#macro>

<sqlMap>

	<#-- add jdbcType for resultMap -->
    <#list tableConfig.resultMaps as resultMap>
    <resultMap id="${resultMap.name}" class="${basepackage}.dataobject.${tableConfig.className}DO">
    <#list resultMap.columns as column>
    	<#if column.javatype?ends_with('Money')>
		<result property="${column.name}.cent" column="${column.name}" javaType="long" nullValue="0" />
    	<#else>
		<result property="${column.name}" column="${column.name}" javaType="${column.javatype}"  <#if column.hasNullValue> nullValue="${column.nullValue}" </#if> />
    	</#if>
	</#list>
    </resultMap>
	</#list>
    
    <resultMap id="RM.${tableConfig.className}" class="${basepackage}.dataobject.${tableConfig.className}DO">
        <#list tableConfig.table.columns as column>
    	<#if column.javaType?ends_with('Money')>
		<result property="${column.columnNameFirstLower}.cent" column="${column.sqlName}" javaType="long" jdbcType="${column.jdbcSqlTypeName}" nullValue="0" />
    	<#else>
		<result property="${column.columnNameFirstLower}" column="${column.sqlName}" javaType="${column.javaType}" jdbcType="${column.jdbcSqlTypeName}" <#if column.hasNullValue> nullValue="${column.nullValue}" </#if> />
    	</#if>
		</#list>
    </resultMap>
    
<#list tableConfig.includeSqls as item>
	<sql id="${item.id}">
		${item.sql?trim}
	</sql>
		
</#list>

<#list tableConfig.sqls as sql>	
<#if sql.selectSql>
	<#if (sql.columnsCount > 1 && !sql.columnsInSameTable)>
	<resultMap id="RM.${sql.resultClassName}" class="${basepackage}.operation.${sql.resultClass}">
    	<#list sql.columns as column>
    	<#if column.javaType?ends_with('Money')>
		<result property="${column.columnNameFirstLower}.cent" column="${column.sqlName}" javaType="long" jdbcType="${column.jdbcSqlTypeName}" nullValue="0" />
    	<#else>
		<result property="${column.columnNameFirstLower}" column="${column.sqlName}" javaType="${column.javaType}" jdbcType="${column.jdbcSqlTypeName}" <#if column.hasNullValue> nullValue="${column.nullValue}" </#if> />
    	</#if>
    	</#list>
	</resultMap>
	</#if>
	
	<#assign selectSqlId> /* <@namespace/>${sql.operation} */ </#assign>	
	<select id="<@namespace/>${sql.operation}" <#if sql.columnsCount == 1>resultClass="${sql.resultClassName}"<#else>resultMap="RM.${sql.resultClassName}"</#if> >
    	<#if sql.hasSqlMap>
    	${StringHelper.insertTokenIntoSelectSql(sql.sqlmap,selectSqlId)}
    	<#else>
    	<@genPageQueryStart sql/>
    	${StringHelper.insertTokenIntoSelectSql(sql.ibatisSql?trim,selectSqlId)}
    	<@genPageQueryEnd sql/>    	
    	</#if>
	</select>	

	<#assign selectSqlIdForPaging> /* <@namespace/>${sql.operation}.count */ </#assign>	
	<#if sql.paging>
	<select id="<@namespace/>${sql.operation}.count" resultClass="long" >
		<#if sql.hasSqlMap>
    	${StringHelper.insertTokenIntoSelectSql(StringHelper.removeIbatisOrderBy(sql.sqlmapCountSql?trim),selectSqlIdForPaging)}
    	<#else>
    	${StringHelper.insertTokenIntoSelectSql(StringHelper.removeIbatisOrderBy(sql.ibatisCountSql?trim),selectSqlIdForPaging)}
    	</#if>
	</select>
	</#if>
	    
</#if>
	
<#if sql.updateSql>
	<update id="<@namespace/>${sql.operation}">
		<#if sql.hasSqlMap>
		${sql.sqlmap}
		<#else>
		${sql.ibatisSql?trim}
		</#if>
	</update>
</#if>
	
<#if sql.deleteSql>
	<delete id="<@namespace/>${sql.operation}">
		<#if sql.hasSqlMap>
		${sql.sqlmap}
		<#else>
		${sql.ibatisSql?trim}
		</#if>
    </delete>
</#if>
    
<#if sql.insertSql>
	<insert id="<@namespace/>${sql.operation}">
		<#if sql.hasSqlMap>
		${sql.sqlmap}
        <#else>             
		${sql.ibatisSql?trim}
        <@genSelectKeyForInsertSql sql/>
        </#if>
	</insert>
</#if>
</#list>

</sqlMap>

<#macro genSelectKeyForInsertSql sql>
	<#if sql.operation != 'insert'>
		<#return>
    </#if>
    <#if databaseType == 'oracle'>
        <#if tableConfig.sequence??>
		<selectKey resultClass="java.lang.Long" type="pre" keyProperty="${tableConfig.pkColumn.columnNameLower}" >
            SELECT ${tableConfig.sequence}.nextval FROM DUAL
        </selectKey>
        </#if>         
    </#if>
    <#if databaseType == 'mysql'>
		<selectKey resultClass="java.lang.Long" type="post" keyProperty="${tableConfig.pkColumn.columnNameLower}" >
            select last_insert_id()
    	</selectKey>        
    </#if> 
    <#if databaseType == 'sqlserver'>
		<selectKey resultClass="java.lang.Long" type="post" keyProperty="${tableConfig.pkColumn.columnNameLower}" >
            SELECT  @@identity  AS  ID
        </selectKey>        
    </#if>                     
</#macro>

<#-- for generate page query -->
<#macro genPageQueryStart sql>
	<#if !sql.paging>
		<#return>
	</#if>
	<#if databaseType == 'oracle'>
			select * from ( select row_.*, rownum rownum_ from (
	</#if>
</#macro>
<#macro genPageQueryEnd sql>
	<#if !sql.paging>
		<#return>
	</#if>
	<#if databaseType == 'oracle'>
			) row_ ) where rownum_ &lt;= #endRow# and rownum_ > #startRow#
	</#if>
	<#if databaseType == 'mysql'>
			limit #offset#,#limit#
	</#if>
	<#if databaseType == 'postgresql'>
			offset #offset# limit #limit#
	</#if>		
</#macro>
