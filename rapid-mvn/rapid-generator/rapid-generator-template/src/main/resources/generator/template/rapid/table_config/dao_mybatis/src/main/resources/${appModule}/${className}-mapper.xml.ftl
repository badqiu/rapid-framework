<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
PUBLIC "-//ibatis.apache.org//DTD Mapper 3.0//EN"
"http://ibatis.apache.org/dtd/ibatis-3-mapper.dtd">
<#assign className=tableConfig.className/>    
<#assign classNameLowerCase=tableConfig.className?lower_case/>    
<#macro namespace>${appName}.${tableConfig.className}.</#macro>

<mapper namespace="${appName}.${tableConfig.className}">

	<#-- add jdbcType for resultMap -->
    <#list tableConfig.resultMaps as resultMap>
    <resultMap id="${resultMap.name}" type="${tableConfig.basepackage}.dataobject.${tableConfig.className}DO">
    <#list resultMap.columns as column>
    	<#if column.javatype?ends_with('Money')>
		<result property="${column.name}.cent" column="${column.name}" javaType="long" />
    	<#else>
		<result property="${column.name}" column="${column.name}" javaType="${column.javatype}"  />
    	</#if>
	</#list>
    </resultMap>
	</#list>
    
    <#-- add nullValue for resultMap -->
    <resultMap id="RM.${tableConfig.className}" type="${tableConfig.basepackage}.dataobject.${tableConfig.className}DO">
        <#list tableConfig.table.columns as column>
    	<#if column.javaType?ends_with('Money')>
		<result property="${column.columnNameFirstLower}.cent" column="${column.sqlName}" javaType="long" jdbcType="${column.jdbcSqlTypeName}" />
    	<#else>
		<result property="${column.columnNameFirstLower}" column="${column.sqlName}" javaType="${column.javaType}" jdbcType="${column.jdbcSqlTypeName}"  />
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
	<resultMap id="RM.${sql.resultClassName}" type="${tableConfig.basepackage}.operation.${classNameLowerCase}.${sql.resultClass}">
    	<#list sql.columns as column>
    	<#if column.javaType?ends_with('Money')>
		<result property="${column.columnNameFirstLower}.cent" column="${column.sqlName}" javaType="long" jdbcType="${column.jdbcSqlTypeName}" nullValue="0" />
    	<#else>
		<result property="${column.columnNameFirstLower}" column="${column.sqlName}" javaType="${column.javaType}" jdbcType="${column.jdbcSqlTypeName}" <#if column.hasNullValue> nullValue="${column.nullValue}" </#if> />
    	</#if>
    	</#list>
	</resultMap>
	</#if>
	
	<#assign selectSqlId> /*<@namespace/>${sql.operation}*/ </#assign>	
	<select id="<@namespace/>${sql.operation}" <@genResultMapOrResultClassForSelectSql sql/> >
    	<#if sql.hasSqlMap>
    	${StringHelper.insertTokenIntoSelectSql(sql.sqlmap,selectSqlId)}
    	<#else>
    	<@genPageQueryStart sql/>
    	${StringHelper.insertTokenIntoSelectSql(sql.ibatisSql?trim,selectSqlId)}
    	<@genPageQueryEnd sql/>    	
    	</#if>
	</select>	

	<#assign selectSqlIdForPaging> /*<@namespace/>${sql.operation}.count*/ </#assign>	
	<#if sql.paging>
	<select id="<@namespace/>${sql.operation}.count" resultType="long" >
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

</mapper>

<#macro genResultMapOrResultClassForSelectSql sql>
	<#compress>
	<#if sql.hasResultMap>
		resultMap="${sql.resultMap}"
	<#elseif sql.columnsCount == 1>
		<#if sql.resultClassName?ends_with('Money') >
		resultMap="RM.Money"
		<#else>
		resultType="${sql.resultClass}"
		</#if>
	<#else>
		resultMap="RM.${sql.resultClassName}"
	</#if>
	</#compress>
</#macro>

<#macro genSelectKeyForInsertSql sql>
	<#if !sql.insertSql>
		<#return>
    </#if>
    <#if (sql.hasSqlMap && sql.sqlmap?contains("</selectKey>")) || sql.ibatisSql?contains("</selectKey>")>
    	<#return>
    </#if>  
    <#if databaseType == 'oracle'>
        <#if tableConfig.sequence??>
		<selectKey resultType="java.lang.Long" order="BEFORE" keyProperty="${tableConfig.pkColumn.columnNameLower}" >
            SELECT ${tableConfig.sequence}.nextval FROM DUAL
        </selectKey>
        </#if>         
    </#if>
    <#if databaseType == 'mysql'>
		<selectKey resultType="java.lang.Long" order="AFTER" keyProperty="${tableConfig.pkColumn.columnNameLower}" >
            select last_insert_id()
    	</selectKey>        
    </#if> 
    <#if databaseType == 'sqlserver'>
		<selectKey resultType="java.lang.Long" order="AFTER" keyProperty="${tableConfig.pkColumn.columnNameLower}" >
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
