<%@page import="${basepackage}.${subpackage}.model.*" %>
<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>

<head>
	<%@ include file="/commons/meta.jsp" %>
	<base href="<%=basePath%>">
	<link href="<@jspEl 'ctx'/>/widgets/extremecomponents/extremecomponents.css" type="text/css" rel=stylesheet>
	<title><%=${className}.TABLE_ALIAS%> 维护</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>
<form action="" method="post">
	<input type="submit" value="新增" onclick="getReferenceForm(this).action='<@jspEl 'ctx'/>${strutsActionBasePath}/create.do'"/>
	<input type="submit" value="查询" onclick="getReferenceForm(this).action='<@jspEl 'ctx'/>${strutsActionBasePath}/query.do'"/>
	<input type="button" value="删除" onclick="batchDelete('<@jspEl 'ctx'/>${strutsActionBasePath}/delete.do','items',document.forms.ec)"/>
</form>

<ec:table items='page.result' var="item" 
	retrieveRowsCallback="limit" sortRowsCallback="limit" filterRowsCallback="limit"
	action="<@jspEl 'ctx'/>${strutsActionBasePath}/list.do" autoIncludeParameters="true">
	<ec:row>
		<ec:column property="选择" title="<input type='checkbox' onclick=\"setAllCheckboxState('items',this.checked)\" >" sortable="false" width="3%" viewsAllowed="html">
			<input type="checkbox" name="items" value="<@generateIdQueryString/>"/>
		</ec:column>
		<#list table.columns as column>
		<#if !column.htmlHidden>
		<ec:column property="${column.columnNameLower}" <#if column.isDateTimeColumn>value="<@jspEl 'item.'+column.columnNameLower+"String"/>"</#if> title="<%=${className}.ALIAS_${column.constantName}%>"/>
		</#if>
		</#list>
		<ec:column property="操作" title="操作" sortable="false" viewsAllowed="html">
			<a href="<@jspEl 'ctx'/>${strutsActionBasePath}/show.do?<@generateIdQueryString/>">查看</a>&nbsp;&nbsp;&nbsp;
			<a href="<@jspEl 'ctx'/>${strutsActionBasePath}/edit.do?<@generateIdQueryString/>">修改</a>
		</ec:column>
	</ec:row>
</ec:table>

</body>

</html>

<#macro generateIdQueryString>
	<#if table.compositeId>
		<#assign itemPrefix = 'item.id.'>
	<#else>
		<#assign itemPrefix = 'item.'>
	</#if>
<#compress>
		<#list table.compositeIdColumns as column>
			<#t>${column.columnNameLower}=<@jspEl itemPrefix + column.columnNameLower/>&
		</#list>				
</#compress>
</#macro>