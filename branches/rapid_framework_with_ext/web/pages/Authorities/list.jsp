<%@page import="com.awd.model.*" %>
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
	<title><%=Authorities.TABLE_ALIAS%> 维护</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>
<form action="" method="post">
	<input type="submit" value="新增" onclick="getReferenceForm(this).action='${ctx}/pages/Authorities/create.do'"/>
	<input type="submit" value="查询" onclick="getReferenceForm(this).action='${ctx}/pages/Authorities/query.do'"/>
	<input type="button" value="删除" onclick="batchDelete('${ctx}/pages/Authorities/delete.do','items',document.forms.ec)"/>
</form>

<ec:table items='page.result' var="item" 
	retrieveRowsCallback="limit" sortRowsCallback="limit" filterRowsCallback="limit"
	action="${ctx}/pages/Authorities/list.do" autoIncludeParameters="true">
	<ec:row>
		<ec:column property="选择" title="<input type='checkbox' onclick=\"setAllCheckboxState('items',this.checked)\" >" sortable="false" width="3%" viewsAllowed="html">
			<input type="checkbox" name="items" value="id=${item.id}&"/>
		</ec:column>
		<ec:column property="name"  title="<%=Authorities.ALIAS_NAME%>"/>
		<ec:column property="displayName"  title="<%=Authorities.ALIAS_DISPLAY_NAME%>"/>
		<ec:column property="操作" title="操作" sortable="false" viewsAllowed="html">
			<a href="${ctx}/pages/Authorities/show.do?id=${item.id}&">查看</a>&nbsp;&nbsp;&nbsp;
			<a href="${ctx}/pages/Authorities/edit.do?id=${item.id}&">修改</a>
		</ec:column>
	</ec:row>
</ec:table>

</body>

</html>

