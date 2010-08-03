<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE sqlMap PUBLIC "-//ibatis.apache.org//DTD SQL Map 2.0//EN" 
    "http://ibatis.apache.org/dtd/sql-map-2.dtd">
    
<#macro namespace>${tableConfig.tableClassName}.</#macro>

<sqlMap>

<#list tableConfig.sqls as sql>	
<#if sql.selectSql>
	<#if (sql.columnsCount > 1 && !sql.columnsInSameTable)>
	<resultMap id="RM.${sql.resultClassName}" class="${basepackage}.query.${sql.resultClass}">
    	<#list sql.columns as column>
		<result property="${column.columnNameFirstLower}" column="${column.sqlName}"/>
    	</#list>
	</resultMap>
	</#if>
		
	<select id="<@namespace/>${sql.operation}" resultMap="<#if sql.columnsCount == 1>${sql.resultClassName}<#else>RM.${sql.resultClassName}</#if>" >
    	<@genPageQueryStart sql/>
    	${sql.ibatisSql?trim}
    	<@genPageQueryEnd sql/>
	</select>	

	<#if sql.paging>
	<select id="<@namespace/>${sql.operation}.count.for.paging" resultClass="long" >
    	${sql.ibatisCountSql?trim}
	</select>
	</#if>
	    
</#if>
	
<#if sql.updateSql>
	<update id="<@namespace/>${sql.operation}">
		${sql.ibatisSql?trim}
	</update>
</#if>
	
<#if sql.deleteSql>
	<delete id="<@namespace/>${sql.operation}">
		${sql.ibatisSql?trim}
    </delete>
</#if>
    
<#if sql.insertSql>
	<insert id="<@namespace/>${sql.operation}">
		${sql.ibatisSql?trim}
        <@genSelectKeyOfInsertSql sql/>             
	</insert>
</#if>
</#list>

</sqlMap>

<#macro genSelectKeyOfInsertSql sql>
	<#if sql.operation == 'insert'>
        <#if databaseType == 'oracle'>
        	<#if tableConfig.sequence??>
		<selectKey resultClass="java.lang.Long" type="pre" keyProperty="${tableConfig.dummypk}" >
            SELECT ${tableConfig.sequence}.nextval FROM DUAL
        </selectKey>
        	</#if>         
        </#if>
        <#if databaseType == 'mysql'>
		<selectKey resultClass="java.lang.Long" type="post" keyProperty="${tableConfig.dummypk}" >
            select last_insert_id()
        </selectKey>        
        </#if> 
        <#if databaseType == 'sqlserver'>
		<selectKey resultClass="java.lang.Long" type="post" keyProperty="${tableConfig.dummypk}" >
            SELECT  @@identity  AS  ID
        </selectKey>        
        </#if>                     
    </#if>
</#macro>

<#macro genPageQueryStart sql>
	<#if sql.paging>
		<#if databaseType == 'oracle'>
		select * from ( select row_.*, rownum rownum_ from (
		</#if>
	</#if>
</#macro>
<#macro genPageQueryEnd sql>
	<#if sql.paging>
		<#if databaseType == 'oracle'>
		) row_ ) where rownum_ <= #endRow# and rownum_ > #startRow#
		</#if>
		<#if databaseType == 'mysql'>
		limit #offset#,#limit#
		</#if>
		<#if databaseType == 'postgre_sql'>
		offset #offset# limit #limit#
		</#if>		
	</#if>
</#macro>
