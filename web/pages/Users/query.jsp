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
	<title><%=Users.TABLE_ALIAS%>查询</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<s:form action="/pages/Users/list.do" method="post">
	<input type="submit" value="提交" onclick="return new Validation(document.forms[0]).validate();"/>
	<input type="button" value="返回列表" onclick="window.location='${ctx}/pages/Users/list.do'"/>
	
	<table class="formTable">
	
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Users.ALIAS_LOGIN_NAME%></td>
		   <td>
				<input  type="text" name="s_loginName" size="30" maxlength="20" class=""/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Users.ALIAS_PASSWORD%></td>
		   <td>
				<input  type="text" name="s_password" size="30" maxlength="20" class=""/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Users.ALIAS_NAME%></td>
		   <td>
				<input  type="text" name="s_name" size="30" maxlength="20" class=""/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Users.ALIAS_EMAIL%></td>
		   <td>
				<input  type="text" name="s_email" size="30" maxlength="30" class=""/>
		   </td>
		</tr>
	
	</table>
</s:form>	
			
</body>

</html>