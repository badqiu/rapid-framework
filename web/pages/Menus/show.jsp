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
	<title><%=Menus.TABLE_ALIAS%>信息</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<s:form action="/pages/Menus/list.do" method="get" theme="simple">
	<input type="button" value="返回列表" onclick="window.location='${ctx}/pages/Menus/list.do'"/>

	<s:hidden name="id" id="id" value="%{model.id}"/>

	<table class="formTable">
		<tr>	
			<td class="tdLabel"><%=Menus.ALIAS_JSBH%></td>	
			<td><s:property value="%{model.jsbh}" /></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=Menus.ALIAS_DESCN%></td>	
			<td><s:property value="%{model.descn}" /></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=Menus.ALIAS_URL%></td>	
			<td><s:property value="%{model.url}" /></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=Menus.ALIAS_IMAGE%></td>	
			<td><s:property value="%{model.image}" /></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=Menus.ALIAS_NAME%></td>	
			<td><s:property value="%{model.name}" /></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=Menus.ALIAS_THE_SORT%></td>	
			<td><s:property value="%{model.theSort}" /></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=Menus.ALIAS_QTIP%></td>	
			<td><s:property value="%{model.qtip}" /></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=Menus.ALIAS_PARENT_ID%></td>	
			<td><s:property value="%{model.parentId}" /></td>
		</tr>
	</table>
</s:form>

</body>

</html>