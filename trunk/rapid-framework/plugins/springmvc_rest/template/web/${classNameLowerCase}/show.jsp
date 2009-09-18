<%@page import="com.company.project.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>

<head>
	<%@ include file="/commons/meta.jsp" %>
	<base href="<%=basePath%>">
	<title><%=UserInfo.TABLE_ALIAS%>信息</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<form:form method="post" action="${ctx}/userinfo.do" modelAttribute="userInfo"  >
	<input type="button" value="返回列表" onclick="window.location='${ctx}/userinfo.do'"/>
	<input type="button" value="后退" onclick="history.back();"/>
	
	<input type="hidden" id="userId" name="userId" value="${userInfo.userId}"/>

	<table class="formTable">
		<tr>	
			<td class="tdLabel"><%=UserInfo.ALIAS_USERNAME%></td>	
			<td><c:out value='${userInfo.username}'/></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=UserInfo.ALIAS_PASSWORD%></td>	
			<td><c:out value='${userInfo.password}'/></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=UserInfo.ALIAS_BIRTH_DATE%></td>	
			<td><c:out value='${userInfo.birthDateString}'/></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=UserInfo.ALIAS_SEX%></td>	
			<td><c:out value='${userInfo.sex}'/></td>
		</tr>
		<tr>	
			<td class="tdLabel"><%=UserInfo.ALIAS_AGE%></td>	
			<td><c:out value='${userInfo.age}'/></td>
		</tr>
	</table>
</form:form>

</body>

</html>