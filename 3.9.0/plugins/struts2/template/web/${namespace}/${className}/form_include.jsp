<%@page import="${basepackage}.model.*" %>
<#include "/macro.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

<#list table.columns as column>
<#if column.htmlHidden>
	<s:hidden id="${column.columnNameLower}" name="${column.columnNameLower}" />
</#if>
</#list>

<!-- ONGL access static field: @package.class@field or @vs@field -->
<#list table.columns as column>
	<#if !column.htmlHidden>
	
	<#if column.isDateTimeColumn>
	<tr>	
		<td class="tdLabel">
			<#if !column.nullable><span class="required">*</span></#if><%=${className}.ALIAS_${column.constantName}%>:
		</td>	
		<td>
		<input value="<@jspEl 'model.'+column.columnNameLower+'String'/>" onclick="WdatePicker({dateFmt:'<%=${className}.FORMAT_${column.constantName}%>'})" id="${column.columnNameLower}String" name="${column.columnNameLower}String"  maxlength="0" class="${column.validateString}" />
		</td>
	</tr>
	<#else>
	<s:textfield label="%{@${basepackage}.model.${className}@ALIAS_${column.constantName}}" key="${column.columnNameLower}" value="%{model.${column.columnNameLower}}" cssClass="${column.validateString}" required="${(!column.nullable)?string}" />
	</#if>
	
	</#if>
</#list>