<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first> 
<#assign classNameLowerCase = className?lower_case> 

<#noparse><#include "/commons/macro.ftl" /></#noparse>

<#noparse><@override name="head"></#noparse>
	<title>${className}.TABLE_ALIAS 信息</title>
<#noparse></@override></#noparse>

<#noparse><@override name="content"></#noparse>
	<form>
		<input type="button" value="返回列表" onclick="window.location='<@jspEl 'ctx'/>/${classNameLowerCase}'"/>
		<input type="button" value="后退" onclick="history.back();"/>

<#list table.columns as column>
<#if column.pk>
	<input type="hidden" id="${column.columnNameLower}" name="${column.columnNameLower}" value="<@jspEl classNameFirstLower+"."+column.columnNameFirstLower/>"/>
</#if>
</#list>

	<table class="formTable">
	<#list table.columns as column>
	<#if !column.htmlHidden>
		<tr>	
			<td class="tdLabel">${className}.ALIAS_${column.constantName}</td>	
			<td><#rt>
			<#compress>
			<#if column.isDateTimeColumn>
			<@jspEl classNameFirstLower+"."+column.columnNameLower+"String!"/>
			<#else>
			<@jspEl classNameFirstLower+"."+column.columnNameLower+"!"/>
			</#if>
			</#compress>
			<#lt></td>
		</tr>
	</#if>
	</#list>
	</table>
	</form>
<#noparse></@override></#noparse>

<#-- freemarker模板继承,具体使用请查看: http://code.google.com/p/rapid-framework/wiki/rapid_freemarker_extends -->
<#noparse><@extends name="*/base.ftl"/></#noparse>