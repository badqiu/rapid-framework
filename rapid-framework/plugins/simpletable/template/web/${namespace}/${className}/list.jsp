<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>无标题文档</title>

<%@ include file="/commons/meta.jsp" %>
<link href="<c:url value="/widgets/simpletable/simpletable.css"/>" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<c:url value="/widgets/simpletable/simpletable.js"/>"></script>

<script type="text/javascript" >
	$(document).ready(function() {
		window.simpleTable = new SimpleTable('simpleTableForm',${page.thisPageNumber},${page.pageSize},'${pageRequest.sortColumns}');
	});
</script>

<body>
 
<form id="simpleTableForm" action="<c:url value="/dim/DactionPath/list.do"/>" method="post">
<input type="hidden" name="pageNumber" id="pageNumber" />
<input type="hidden" name="pageSize" id="pageSize"/>
<input type="hidden" name="sortColumns" id="sortColumns"/>

<div class="gridTable">

	<%pageContext.setAttribute("pageSizeSelectList",new Integer[]{10,50,100,200}); %>
	<simpletable:pageToolbar pageSizeSelectList="${pageSizeSelectList}" page="${page}"></simpletable:pageToolbar>

	<table width="100%"  border="0" cellspacing="0" class="gridBody">
	  <thead class="tableHeader">
		  <tr>
			<th style="width:1%;"> </th>
			<th style="width:1%;"><input type="checkbox" onclick="setAllCheckboxState('items',this.checked)"></th>
			<th sortColumn="action_path_code" >原始路径</th>
			<th sortColumn="action_path" >自定义路径</th>
			<th sortColumn="action_path_desc" >描述</th>
			<th sortColumn="deep" >深度</th>
			<th>操作</th>
		  </tr>
	  </thead>
	  <tbody class="tableBody">
	  	  <c:forEach items="${page.result}" var="item" varStatus="status">
		  <tr class="${status.count % 2 == 0 ? 'odd' : 'even' }">
			<td>${page.thisPageFirstElementNumber + status.index}</td>
			<td><input type="checkbox" name="items" value="checkbox"></td>
			<td>${item.actionPathCode}</td>
			<td>${item.actionPath}</td>
			<td>${item.actionPathDesc}&nbsp;</td>
			<td>${item.deep}&nbsp;</td>
			<td><a href="">查看</a>&nbsp;&nbsp;&nbsp;<a href="">编辑</a></td>
		  </tr>
	  	  </c:forEach>
	  </tbody>
	</table>

	<simpletable:pageToolbar pageSizeSelectList="${pageSizeSelectList}" page="${page}">
		<span class="button_label"> Filter Source: </span>
		<select id="basic_filter_drpdown"
			onchange="table._toggleFilterType(this.options[this.selectedIndex].value)">
			<option value="0" selected>
				containing
			</option>
			<option value="1">
				excluding
			</option>
		</select>
		<input name="query" type="text" value="" ">
		<a href="#" onclick="table._filter(); return false;" class="button" title="Go"> <b><b><b>Go</b> </b> </b> </a>
	</simpletable:pageToolbar>
	
</div>
</form>

</body>
</html>
