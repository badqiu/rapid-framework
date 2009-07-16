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
	<title><%=Userinfo.TABLE_ALIAS%>信息</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<s:form action="/pages/Userinfo/list.do" method="get" theme="simple">
	<input type="button" value="返回列表" onclick="window.location='${ctx}/pages/Userinfo/list.do'"/>

	<s:hidden name="id" id="id" value="%{model.id}"/>

	<table class="formTable">
		<tr>	
			<td class="tdLabel"><%=Userinfo.ALIAS_USERNAME%></td>	
			<td><s:property value="%{model.username}" /></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=Userinfo.ALIAS_USERPHONE%></td>	
			<td><s:property value="%{model.userphone}" /></td>
		</tr>
	</table>
</s:form>

</body>

</html>