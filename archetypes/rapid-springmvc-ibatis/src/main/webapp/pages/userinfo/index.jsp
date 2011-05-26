<%@page import="com.company.project.domain.*" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/simpletable" prefix="simpletable"%>
<%@ include file="/commons/taglibs.jsp" %>

<rapid:override name="head">
	<title><%=UserInfo.TABLE_ALIAS%> 维护</title>
	
	<script src="${ctx}/scripts/rest.js" ></script>
	<link href="<c:url value="/widgets/simpletable/simpletable.css"/>" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="<c:url value="/widgets/simpletable/simpletable.js"/>"></script>
	
	<script type="text/javascript" >
		$(document).ready(function() {
			// 分页需要依赖的初始化动作
			window.simpleTable = new SimpleTable('queryForm',${page.thisPageNumber},${page.pageSize},'${pageQuery.sortColumns}');
		});
	</script>
</rapid:override>

<rapid:override name="content">
	<form id="queryForm" name="queryForm" method="get" style="display: inline;">
	<div class="queryPanel">
		<fieldset>
			<legend>搜索</legend>
			<table>
				<tr>	
					<td class="tdLabel"><%=UserInfo.ALIAS_USERNAME%></td>		
					<td>
						<input value="${query.username}" id="username" name="username" maxlength="50"  class=""/>
					</td>
					<td class="tdLabel"><%=UserInfo.ALIAS_PASSWORD%></td>		
					<td>
						<input value="${query.password}" id="password" name="password" maxlength="50"  class=""/>
					</td>
					<td class="tdLabel"><%=UserInfo.ALIAS_BIRTH_DATE%></td>		
					<td>
						<input value="<fmt:formatDate value='${query.birthDateBegin}' pattern='<%=UserInfo.FORMAT_BIRTH_DATE%>'/>" onclick="WdatePicker({dateFmt:'<%=UserInfo.FORMAT_BIRTH_DATE%>'})" id="birthDateBegin" name="birthDateBegin"   />
						<input value="<fmt:formatDate value='${query.birthDateEnd}' pattern='<%=UserInfo.FORMAT_BIRTH_DATE%>'/>" onclick="WdatePicker({dateFmt:'<%=UserInfo.FORMAT_BIRTH_DATE%>'})" id="birthDateEnd" name="birthDateEnd"   />
					</td>
					<td class="tdLabel"><%=UserInfo.ALIAS_SEX%></td>		
					<td>
						<input value="${query.sex}" id="sex" name="sex" maxlength="10"  class="validate-integer max-value-2147483647"/>
					</td>
				</tr>	
				<tr>	
					<td class="tdLabel"><%=UserInfo.ALIAS_AGE%></td>		
					<td>
						<input value="${query.age}" id="age" name="age" maxlength="10"  class="validate-integer max-value-2147483647"/>
					</td>
				</tr>	
			</table>
		</fieldset>
		<div class="handleControl">
			<input type="submit" class="stdButton" style="width:80px" value="查询" onclick="getReferenceForm(this).action='${ctx}/userinfo'"/>
			<input type="button" class="stdButton" style="width:80px" value="新增" onclick="window.location = '${ctx}/userinfo/new'"/>
			<input type="button" class="stdButton" style="width:80px" value="删除" onclick="doRestBatchDelete('${ctx}/userinfo','items',document.forms.queryForm)"/>
		<div>
	
	</div>
	
	<div class="gridTable">
	
		<simpletable:pageToolbar page="${page}">
		显示在这里是为了提示你如何自定义表头,可修改模板删除此行
		</simpletable:pageToolbar>
	
		<table width="100%"  border="0" cellspacing="0" class="gridBody">
		  <thead>
			  
			  <tr>
				<th style="width:1px;"> </th>
				<th style="width:1px;"><input type="checkbox" onclick="setAllCheckboxState('items',this.checked)"></th>
				
				<!-- 排序时为th增加sortColumn即可,new SimpleTable('sortColumns')会为tableHeader自动增加排序功能; -->
				<th sortColumn="USERNAME" ><%=UserInfo.ALIAS_USERNAME%></th>
				<th sortColumn="PASSWORD" ><%=UserInfo.ALIAS_PASSWORD%></th>
				<th sortColumn="BIRTH_DATE" ><%=UserInfo.ALIAS_BIRTH_DATE%></th>
				<th sortColumn="SEX" ><%=UserInfo.ALIAS_SEX%></th>
				<th sortColumn="AGE" ><%=UserInfo.ALIAS_AGE%></th>
	
				<th>操作</th>
			  </tr>
			  
		  </thead>
		  <tbody>
		  	  <c:forEach items="${page.result}" var="item" varStatus="status">
		  	  
			  <tr class="${status.count % 2 == 0 ? 'odd' : 'even'}">
				<td>${page.thisPageFirstElementNumber + status.index}</td>
				<td><input type="checkbox" name="items" value="${item.userId}"></td>
				
				<td><c:out value='${item.username}'/>&nbsp;</td>
				<td><c:out value='${item.password}'/>&nbsp;</td>
				<td><c:out value='${item.birthDateString}'/>&nbsp;</td>
				<td><c:out value='${item.sex}'/>&nbsp;</td>
				<td><c:out value='${item.age}'/>&nbsp;</td>
				<td>
					<a href="${ctx}/userinfo/${item.userId}">查看</a>&nbsp;&nbsp;
					<a href="${ctx}/userinfo/${item.userId}/edit">修改</a>&nbsp;&nbsp;
					<a href="${ctx}/userinfo/${item.userId}" onclick="doRestDelete(this,'你确认删除?');return false;">删除</a>
				</td>
			  </tr>
			  
		  	  </c:forEach>
		  </tbody>
		</table>
	
		<simpletable:pageToolbar page="${page}">
		显示在这里是为了提示你如何自定义表头,可修改模板删除此行
		</simpletable:pageToolbar>
		
	</div>
	</form>
</rapid:override>

<%-- jsp模板继承,具体使用请查看: http://code.google.com/p/rapid-framework/wiki/rapid_jsp_extends --%>
<%@ include file="base.jsp" %>
