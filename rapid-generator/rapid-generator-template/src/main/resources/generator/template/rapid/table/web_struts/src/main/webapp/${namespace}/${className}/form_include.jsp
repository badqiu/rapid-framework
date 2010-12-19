<%@page import="${basepackage}.model.*" %>
<#include "/macro.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

<#list table.columns as column>
<#if column.htmlHidden>
	<html:hidden styleId="${column.columnNameLower}" property="${column.columnNameLower}" name="${className}Form"/>
</#if>
</#list>

<#list table.columns as column>
	<#if !column.htmlHidden>
	
	<tr>
		<td class="tdLabel">
			<#if !column.nullable><span class="required">*</span></#if><%=${className}.ALIAS_${column.constantName}%>:
		</td>
		<td>
	<#if column.isDateTimeColumn>
		<input value="<@jspEl className+'Form.'+column.columnNameLower+'String'/>" onclick="WdatePicker({dateFmt:'<%=${className}.FORMAT_${column.constantName}%>'})"  id="${column.columnNameLower}String" name="${column.columnNameLower}String"  maxlength="0" class="${column.validateString}" />
	<#else>
		<html:text  styleId="${column.columnNameLower}" property="${column.columnNameLower}" name="${className}Form"  maxlength="${column.size}" styleClass="${column.validateString}" />
	</#if>
		</td>
	</tr>
	</#if>
</#list>

