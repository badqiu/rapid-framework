<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first> 
<#assign classNameLowerCase = className?lower_case> 

<#noparse><#include "/commons/macro.ftl" /></#noparse>

<#noparse><@override name="head"></#noparse>
	<title>UserInfo.TABLE_ALIAS 管理</title>
	<#noparse>
	<link href="${ctx}/widgets/simpletable/simpletable.css" type="text/css" rel="stylesheet">
	<script src="${ctx}/widgets/simpletable/simpletable.js" type="text/javascript"></script>
	
	<script type="text/javascript" >
		$(document).ready(function() {
			// 分页需要依赖的初始化动作
			window.simpleTable = new SimpleTable('simpleTableForm','${page.thisPageNumber!}','${page.pageSize!}','${pageRequest.sortColumns!}');
		});
	</script>
	</#noparse>
<#noparse></@override></#noparse>

<#noparse><@override name="content"></#noparse>

<div class="queryPanel">
<form method="get" style="display: inline;">
<fieldset>
	<legend>搜索</legend>
	<table>
		<#list table.columns?chunk(5) as row>
		<tr>	
			<#list row as column>
			<#if !column.htmlHidden>	
			<td class="tdLabel">
					${className}.ALIAS_${column.constantName}
			</td>		
			<td>
				<#if column.isDateTimeColumn>
				<input value="<@jspEl "pageRequest.filters."+column.columnNameLower+"!"/>" onclick="WdatePicker({dateFmt:'<%=${className}.FORMAT_${column.constantName}%>'})"  name="s_${column.columnNameLower}"   />
				<#else>
				<input value="<@jspEl "pageRequest.filters."+column.columnNameLower+"!"/>"  name="s_${column.columnNameLower}"  />
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
	<input type="button" class="stdButton" style="width:80px" value="删除" onclick="doRestBatchDelete('<@jspEl 'ctx'/>/${classNameLowerCase}','items',document.forms.simpleTableForm)"/>
<div>
</form>
</div>

<form id="simpleTableForm" action="<@jspEl 'ctx'/>/userinfo" method="get" style="display: inline;">

	<#noparse>
	<!-- auto include parameters -->
	<#list pageRequest.filters?keys as key>
		<input type="hidden" name="s_${key}" value="${pageRequest.filters[key]}"/>
	</#list>
	</#noparse>
	
	<input type="hidden" name="pageNumber" id="pageNumber" />
	<input type="hidden" name="pageSize" id="pageSize"/>
	<input type="hidden" name="sortColumns" id="sortColumns"/>
	
	<div class="gridTable">
	
		<#noparse><@pageToolBar page=page></#noparse>
		显示在这里是为了提示你如何自定义表头,可修改模板删除此行
		<#noparse></@pageToolBar></#noparse>
	
		<table width="100%"  border="0" cellspacing="0" class="gridBody">
		  <thead class="tableHeader">
			  
			  <tr>
				<th style="width:1px;"> </th>
				<th style="width:1px;"><input type="checkbox" onclick="setAllCheckboxState('items',this.checked)"></th>
				
				<!-- 排序时为th增加sortColumn即可,new SimpleTable('sortColumns')会为tableHeader自动增加排序功能; -->
				<#list table.columns as column>
				<#if !column.htmlHidden>
				<th sortColumn="${column.sqlName}" >${className}.ALIAS_${column.constantName}</th>
				</#if>
				</#list>
				
				<th>操作</th>
			  </tr>
			  
		  </thead>
		  <tbody class="tableBody">
		  	  <#noparse>
		  	  <#list page.result as item>
			  <tr class="<#if item_index % 2 == 0>odd<#else>even</#if>">
			  </#noparse>
				<td><#noparse>${page.thisPageFirstElementNumber + item_index + 1}</#noparse></td>
				<td><input type="checkbox" name="items" value="<@jspEl 'item.'+table.idColumn.columnNameFirstLower/>"></td>
				
				<#list table.columns as column>
				<#if !column.htmlHidden>
				<td><#rt>
					<#compress>
					<#if column.isDateTimeColumn>
					<@jspEl "item."+column.columnNameLower+"String!"/>&nbsp;
					<#else>
					<@jspEl "item."+column.columnNameLower+"!"/>&nbsp;
					</#if>
					</#compress>
				<#lt></td>
				</#if>
				</#list>
								
				<td>
					<a href="<@jspEl 'ctx'/>/userinfo/<@jspEl 'item.'+table.idColumn.columnNameFirstLower/>">查看</a>&nbsp;&nbsp;
					<a href="<@jspEl 'ctx'/>/userinfo/<@jspEl 'item.'+table.idColumn.columnNameFirstLower/>/edit">修改</a>&nbsp;&nbsp;
					<a href="<@jspEl 'ctx'/>/userinfo/<@jspEl 'item.'+table.idColumn.columnNameFirstLower/>" onclick="doRestDelete(this,'你确认删除?');return false;">删除</a>
				</td>
			  </tr>
			  <#noparse></#list></#noparse>
		  </tbody>
		</table>
	
		<#noparse><@pageToolBar page=page></#noparse>
		显示在这里是为了提示你如何自定义表头,可修改模板删除此行
		<#noparse></@pageToolBar></#noparse>
		
	</div>
</form>
<#noparse></@override></#noparse>

<#noparse><@extends name="/base.ftl"/></#noparse>
