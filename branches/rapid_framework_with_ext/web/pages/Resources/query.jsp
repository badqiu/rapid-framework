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
	<title><%=Resources.TABLE_ALIAS%>查询</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<s:form action="/pages/Resources/list.do" method="post">
	<input type="submit" value="提交" onclick="return new Validation(document.forms[0]).validate();"/>
	<input type="button" value="返回列表" onclick="window.location='${ctx}/pages/Resources/list.do'"/>
	
	<table class="formTable">
	
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Resources.ALIAS_RESOURCE_TYPE%></td>
		   <td>
				<input  type="text" name="s_resourceType" size="30" maxlength="20" class=""/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Resources.ALIAS_VALUE%></td>
		   <td>
				<input  type="text" name="s_value" size="30" maxlength="255" class=""/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Resources.ALIAS_ORDER_NUM%></td>
		   <td>
				<input  type="text" name="s_orderNum" size="30" maxlength="10" class="validate-integer "/>
		   </td>
		</tr>
	
	</table>
</s:form>	
			
</body>

</html>