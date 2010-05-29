<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first> 
<#assign classNameLowerCase = className?lower_case> 
<%@page import="${basepackage}.model.*" %>
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
	<script src="<@jspEl 'ctx'/>/scripts/rest.js" ></script>
	<title><%=${className}.TABLE_ALIAS%> 维护</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<div class="queryPanel">
<form id="queryForm" name="queryForm" method="get" style="display: inline;">
<input type="hidden" value="${query.pageNumber}" name="pageNumber"/>
<input type="hidden" value="${query.pageSize}" name="pageSize"/>
<input type="hidden" value="${query.sortColumns}" name="sortColumns"/>
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
				<input value="<@jspEl "query."+column.columnNameLower+'Begin'/>" onclick="WdatePicker({dateFmt:'<%=${className}.FORMAT_${column.constantName}%>'})"  name="${column.columnNameLower}"   />
				<input value="<@jspEl "query."+column.columnNameLower+'End'/>" onclick="WdatePicker({dateFmt:'<%=${className}.FORMAT_${column.constantName}%>'})"  name="${column.columnNameLower}"   />
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
	<input type="submit" class="stdButton" style="width:80px" value="查询" onclick="getReferenceForm(this).action='<@jspEl 'ctx'/>/${classNameLowerCase}'"/>
	<input type="button" class="stdButton" style="width:80px" value="新增" onclick="window.location = '<@jspEl 'ctx'/>/${classNameLowerCase}/new'"/>
	<input type="button" class="stdButton" style="width:80px" value="删除" onclick="doRestBatchDelete('<@jspEl 'ctx'/>/${classNameLowerCase}','items',document.forms.ec)"/>
<div>
</form>
</div>

<ec:table items='page.result' var="item" method="get" form="queryForm"
	retrieveRowsCallback="limit" sortRowsCallback="limit" filterRowsCallback="limit"
	action="<@jspEl 'ctx'/>/${classNameLowerCase}" autoIncludeParameters="false">
	<ec:row>
		<ec:column property="选择" title="<input type='checkbox' onclick=\"setAllCheckboxState('items',this.checked)\" >" sortable="false" width="3%" viewsAllowed="html">
			<input type="checkbox" name="items" value="<@jspEl 'item.'+table.idColumn.columnNameFirstLower/>"/>
		</ec:column>
		<#list table.columns as column>
		<#if !column.htmlHidden>
		<ec:column property="${column.columnNameLower}" <#if column.isDateTimeColumn>value="<@jspEl 'item.'+column.columnNameLower+"String"/>"</#if> title="<%=${className}.ALIAS_${column.constantName}%>"/>
		</#if>
		</#list>
		<ec:column property="操作" title="操作" sortable="false" viewsAllowed="html">
			<a href="<@jspEl 'ctx'/>/${classNameLowerCase}/<@jspEl 'item.'+table.idColumn.columnNameFirstLower/>">查看</a>&nbsp;&nbsp;
			<a href="<@jspEl 'ctx'/>/${classNameLowerCase}/<@jspEl 'item.'+table.idColumn.columnNameFirstLower/>/edit">修改</a>&nbsp;&nbsp;
			<a href="<@jspEl 'ctx'/>/${classNameLowerCase}/<@jspEl 'item.'+table.idColumn.columnNameFirstLower/>" onclick="doRestDelete(this,'你确认删除?');return false;">删除</a>
		</ec:column>
	</ec:row>
</ec:table>

</body>

</html>

