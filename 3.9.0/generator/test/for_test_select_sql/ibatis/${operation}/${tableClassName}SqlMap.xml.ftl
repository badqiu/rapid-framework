
<#macro namespace>${sql.tableClassName}.</#macro>
	
<#if sql.selectSql>
	<#if sql.columnsCount == 1>
	<select id="<@namespace/>${sql.operation}" resultMap="${sql.resultClassName}" >
    <![CDATA[
	${sql.ibatisSql}
    ]]>
	</select>	
	<#else>
	
	<#if !sql.columnsInSameTable>
	<resultMap id="RM-${sql.resultClassName}" class="${sql.resultClassName}">
    <#list sql.columns as column>
		<result property="${column.columnNameFirstLower}" column="${column.sqlName}"/>
    </#list>
	</resultMap>
	</#if>
	
	<select id="<@namespace/>${sql.operation}" resultMap="RM.${sql.resultClassName}" >
    <![CDATA[
	${sql.ibatisSql}
    ]]>
	</select>	  	
	</#if>
    
</#if>
	
<#if sql.updateSql>
	<update id="UserInfo.update">
    <![CDATA[
	${sql.ibatisSql}
    ]]>
	</update>
</#if>
	
<#if sql.deleteSql>
	<delete id="UserInfo.delete">
    <![CDATA[
	${sql.ibatisSql}
    ]]>
    </delete>
</#if>
    
<#if sql.insertSql>
	<insert id="UserInfo.insert">
    <![CDATA[
	${sql.ibatisSql}
    ]]>
	</insert>
</#if>