<%@page import="${basepackage}.model.*" %>
<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<%@page import="com.company.project.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>

<head>
	<%@ include file="/commons/meta.jsp" %>
	<base href="<%=basePath%>">
	<link href="${ctx}/widgets/extremecomponents/extremecomponents.css" type="text/css" rel=stylesheet>
	<title><%=UserInfo.TABLE_ALIAS%> 维护</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<div class="queryPanel">
<form action="<c:url value="/pages/UserInfo/list.do"/>" method="get" style="display: inline;">
<fieldset>
	<legend>搜索</legend>
	<table>
		<tr>	
			<td class="tdLabel">
					<%=UserInfo.ALIAS_USERNAME%>
			</td>		
			<td>
				<input value="${pageRequest.filters.username}"  name="s_username"  />
			</td>
			<td class="tdLabel">
					<%=UserInfo.ALIAS_PASSWORD%>
			</td>		
			<td>
				<input value="${pageRequest.filters.password}"  name="s_password"  />
			</td>
			<td class="tdLabel">
					<%=UserInfo.ALIAS_BIRTH_DATE%>
			</td>		
			<td>
				<input value="${pageRequest.filters.birthDate}" onclick="WdatePicker({dateFmt:'<%=UserInfo.FORMAT_BIRTH_DATE%>'})"  name="s_birthDate"   />
			</td>
			<td class="tdLabel">
					<%=UserInfo.ALIAS_SEX%>
			</td>		
			<td>
				<input value="${pageRequest.filters.sex}"  name="s_sex"  />
			</td>
		</tr>	
		<tr>	
			<td class="tdLabel">
					<%=UserInfo.ALIAS_AGE%>
			</td>		
			<td>
				<input value="${pageRequest.filters.age}"  name="s_age"  />
			</td>
		</tr>	
	</table>
</fieldset>
<div class="handleControl">
	<input type="submit" class="stdButton" style="width:80px" value="查询" onclick="getReferenceForm(this).action='${ctx}/userinfo.do'"/>
	<input type="button" class="stdButton" style="width:80px" value="新增" onclick="window.location = '${ctx}/userinfo/new.do'"/>
	<input type="button" class="stdButton" style="width:80px" value="删除" onclick="doRestBatchDelete('${ctx}/userinfo.do','items',document.forms.ec)"/>
<div>
</form>
</div>

<ec:table items='page.result' var="item" method="get"
	retrieveRowsCallback="limit" sortRowsCallback="limit" filterRowsCallback="limit"
	action="${ctx}/userinfo.do" autoIncludeParameters="true">
	<ec:row>
		<ec:column property="选择" title="<input type='checkbox' onclick=\"setAllCheckboxState('items',this.checked)\" >" sortable="false" width="3%" viewsAllowed="html">
			<input type="checkbox" name="items" value="${item.userId}"/>
		</ec:column>
		<ec:column property="username"  title="<%=UserInfo.ALIAS_USERNAME%>"/>
		<ec:column property="password"  title="<%=UserInfo.ALIAS_PASSWORD%>"/>
		<ec:column property="birthDate" value="${item.birthDateString}" title="<%=UserInfo.ALIAS_BIRTH_DATE%>"/>
		<ec:column property="sex"  title="<%=UserInfo.ALIAS_SEX%>"/>
		<ec:column property="age"  title="<%=UserInfo.ALIAS_AGE%>"/>
		<ec:column property="操作" title="操作" sortable="false" viewsAllowed="html">
			<a href="${ctx}/userinfo/${item.userId}.do">显示</a>&nbsp;&nbsp;
			<a href="${ctx}/userinfo/${item.userId}/edit.do">修改</a>&nbsp;&nbsp;
			<a href="${ctx}/userinfo/${item.userId}.do" onclick="doRestDelete(this,'你确认删除?');return false;">删除</a>
		</ec:column>
	</ec:row>
</ec:table>

</body>

</html>

