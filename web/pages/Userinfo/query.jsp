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
	<title><%=Userinfo.TABLE_ALIAS%>查询</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<s:form action="/pages/Userinfo/list.do" method="post">
	<input type="submit" value="提交" onclick="return new Validation(document.forms[0]).validate();"/>
	<input type="button" value="返回列表" onclick="window.location='${ctx}/pages/Userinfo/list.do'"/>
	
	<table class="formTable">
	
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Userinfo.ALIAS_USERNAME%></td>
		   <td>
				<input  type="text" name="s_username" size="30" maxlength="50" class=""/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Userinfo.ALIAS_USERPHONE%></td>
		   <td>
				<input  type="text" name="s_userphone" size="30" maxlength="20" class=""/>
		   </td>
		</tr>
	
	</table>
</s:form>	
			
</body>

</html>