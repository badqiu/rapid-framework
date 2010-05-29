<%@page import="${basepackage}.model.*" %>
<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<#assign actionExtension = "do"> 
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
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

<div class="queryPanel">
<form action="<c:url value="${actionBasePath}/list.do"/>" method="get" style="display: inline;">
<fieldset>
	<legend>搜索</legend>
	<table>
		<#list table.columns?chunk(5) as row>
		<tr>	
			<#list row as column>
			<#if !column.htmlHidden>	
			<td class="tdLabel">
					<%=${className}.ALIAS_${column.constantName}%>
			</td>		
			<td>
				<#if column.isDateTimeColumn>
				<input value="<@jspEl "query."+column.columnNameLower+"Begin"/>" onclick="WdatePicker({dateFmt:'<%=${className}.FORMAT_${column.constantName}%>'})"  name="${column.columnNameLower}Begin"   />
				<input value="<@jspEl "query."+column.columnNameLower+"End"/>" onclick="WdatePicker({dateFmt:'<%=${className}.FORMAT_${column.constantName}%>'})"  name="${column.columnNameLower}End"   />
				<#else>
				<input value="<@jspEl "query."+column.columnNameLower/>"  name="${column.columnNameLower}"  />
				</#if>
			</td>
			</#if>
			</#list>
		</tr>	
		</#list>			
	</table>
</fieldset>
<div class="handleControl">
	<input type="submit" class="stdButton" style="width:80px" value="查询" onclick="getReferenceForm(this).action='<@jspEl 'ctx'/>${actionBasePath}/list.do'"/>
	<input type="submit" class="stdButton" style="width:80px" value="新增" onclick="getReferenceForm(this).action='<@jspEl 'ctx'/>${actionBasePath}/create.do'"/>
	<input type="button" class="stdButton" style="width:80px" value="删除" onclick="batchDelete('<@jspEl 'ctx'/>${actionBasePath}/delete.do','items',document.forms.ec)"/>
<div>
</form>
</div>

<ec:table items='page.result' var="item" method="get"
	retrieveRowsCallback="limit" sortRowsCallback="limit" filterRowsCallback="limit"
	action="<@jspEl 'ctx'/>${actionBasePath}/list.${actionExtension}" autoIncludeParameters="true">
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
			<a href="<@jspEl 'ctx'/>${actionBasePath}/show.${actionExtension}?<@generateIdQueryString/>">查看</a>&nbsp;&nbsp;&nbsp;
			<a href="<@jspEl 'ctx'/>${actionBasePath}/edit.${actionExtension}?<@generateIdQueryString/>">修改</a>
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