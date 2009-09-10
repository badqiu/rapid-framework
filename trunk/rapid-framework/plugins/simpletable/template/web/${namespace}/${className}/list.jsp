<%@page import="${basepackage}.model.*" %>
<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
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
	
	<link href="<c:url value="/widgets/simpletable/simpletable.css"/>" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="<c:url value="/widgets/simpletable/simpletable.js"/>"></script>
	
	<script type="text/javascript" >
		$(document).ready(function() {
			// 分页需要依赖的初始化动作
			window.simpleTable = new SimpleTable('simpleTableForm',<@jspEl 'page.thisPageNumber'/>,<@jspEl 'page.pageSize'/>,'<@jspEl 'pageRequest.sortColumns'/>');
		});
	</script>

</head>
<body>

<%@ include file="/commons/messages.jsp" %>

<div class="queryPanel">
<form action="<c:url value="${actionBasePath}/list.do"/>" method="get" style="display: inline;">
<fieldset>
	<legend>搜索</legend>
	<table>
		<#list table.notPkColumns?chunk(4) as row>
		<tr>	
			<#list row as column>
			<#if !column.htmlHidden>	
			<td class="tdLabel"><%=${className}.ALIAS_${column.constantName}%></td>		
			<td>
				<#if column.isDateTimeColumn>
				<input value="<@jspEl "pageRequest.filters."+column.columnNameLower/>" onclick="WdatePicker({dateFmt:'<%=${className}.FORMAT_${column.constantName}%>'})" id="s_${column.columnNameLower}" name="s_${column.columnNameLower}"   />
				<#else>
				<input value="<@jspEl "pageRequest.filters."+column.columnNameLower/>" id="${column.columnNameLower}" name="s_${column.columnNameLower}" maxlength="${column.size}"  class="${column.noRequiredValidateString}"/>
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
	<input type="button" class="stdButton" style="width:80px" value="删除" onclick="batchDelete('<@jspEl 'ctx'/>${actionBasePath}/delete.do','items',document.forms.simpleTableForm)"/>
<div>
</form>
</div>

<form id="simpleTableForm" action="<c:url value="${actionBasePath}/list.do"/>" method="get" style="display: inline;">

	<!-- auto include parameters -->
	<c:forEach items="<@jspEl 'pageRequest.filters'/>" var="entry">
	<input type="hidden" name="s_<@jspEl 'entry.key'/>" value="<@jspEl 'entry.value'/>"/>
	</c:forEach>
	
	<input type="hidden" name="pageNumber" id="pageNumber" />
	<input type="hidden" name="pageSize" id="pageSize"/>
	<input type="hidden" name="sortColumns" id="sortColumns"/>
	
	<div class="gridTable">
	
		<simpletable:pageToolbar page="<@jspEl 'page'/>">
		显示在这里是为了提示你如何自定义表头,可修改模板删除此行
		</simpletable:pageToolbar>
	
		<table width="100%"  border="0" cellspacing="0" class="gridBody">
		  <thead class="tableHeader">
			  
			  <tr>
				<th style="width:1px;"> </th>
				<th style="width:1px;"><input type="checkbox" onclick="setAllCheckboxState('items',this.checked)"></th>
				
				<!-- 排序时为th增加sortColumn即可,new SimpleTable('sortColumns')会为tableHeader自动增加排序功能; -->
				<#list table.columns as column>
				<#if !column.htmlHidden>
				<th sortColumn="${column.sqlName}" ><%=${className}.ALIAS_${column.constantName}%></th>
				</#if>
				</#list>

				<th>操作</th>
			  </tr>
			  
		  </thead>
		  <tbody class="tableBody">
		  	  <c:forEach items="<@jspEl 'page.result'/>" var="item" varStatus="status">
		  	  
			  <tr class="<@jspEl "status.count % 2 == 0 ? 'odd' : 'even'"/>">
				<td><@jspEl 'page.thisPageFirstElementNumber + status.index'/></td>
				<td><input type="checkbox" name="items" value="checkbox"></td>
				
				<#list table.columns as column>
				<#if !column.htmlHidden>
				<td><#rt>
					<#compress>
					<#if column.isDateTimeColumn>
					<c:out value='<@jspEl "item."+column.columnNameLower+"String"/>'/>&nbsp;
					<#else>
					<c:out value='<@jspEl "item."+column.columnNameLower/>'/>&nbsp;
					</#if>
					</#compress>
				<#lt></td>
				</#if>
				</#list>
				<td>
					<a href="<@jspEl 'ctx'/>${actionBasePath}/show.do?<@generateIdQueryString/>">查看</a>&nbsp;&nbsp;&nbsp;
					<a href="<@jspEl 'ctx'/>${actionBasePath}/edit.do?<@generateIdQueryString/>">修改</a>
				</td>
			  </tr>
			  
		  	  </c:forEach>
		  </tbody>
		</table>
	
		<simpletable:pageToolbar page="<@jspEl 'page'/>">
		显示在这里是为了提示你如何自定义表头,可修改模板删除此行
		</simpletable:pageToolbar>
		
	</div>
</form>

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