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
	<title><%=Resources.TABLE_ALIAS%>信息</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<s:form action="/pages/Resources/list.do" method="get" theme="simple">
	<input type="button" value="返回列表" onclick="window.location='${ctx}/pages/Resources/list.do'"/>

	<s:hidden name="id" id="id" value="%{model.id}"/>

	<table class="formTable">
		<tr>	
			<td class="tdLabel"><%=Resources.ALIAS_RESOURCE_TYPE%></td>	
			<td><s:property value="%{model.resourceType}" /></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=Resources.ALIAS_VALUE%></td>	
			<td><s:property value="%{model.value}" /></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=Resources.ALIAS_ORDER_NUM%></td>	
			<td><s:property value="%{model.orderNum}" /></td>
		</tr>
	</table>
</s:form>

</body>

</html>