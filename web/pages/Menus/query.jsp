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
	<title><%=Menus.TABLE_ALIAS%>查询</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<s:form action="/pages/Menus/list.do" method="post">
	<input type="submit" value="提交" onclick="return new Validation(document.forms[0]).validate();"/>
	<input type="button" value="返回列表" onclick="window.location='${ctx}/pages/Menus/list.do'"/>
	
	<table class="formTable">
	
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Menus.ALIAS_JSBH%></td>
		   <td>
				<input  type="text" name="s_jsbh" size="30" maxlength="9" class="validate-integer "/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Menus.ALIAS_DESCN%></td>
		   <td>
				<input  type="text" name="s_descn" size="30" maxlength="50" class=""/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Menus.ALIAS_URL%></td>
		   <td>
				<input  type="text" name="s_url" size="30" maxlength="50" class=""/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Menus.ALIAS_IMAGE%></td>
		   <td>
				<input  type="text" name="s_image" size="30" maxlength="50" class=""/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Menus.ALIAS_NAME%></td>
		   <td>
				<input  type="text" name="s_name" size="30" maxlength="50" class=""/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Menus.ALIAS_THE_SORT%></td>
		   <td>
				<input  type="text" name="s_theSort" size="30" maxlength="4" class="validate-integer "/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Menus.ALIAS_QTIP%></td>
		   <td>
				<input  type="text" name="s_qtip" size="30" maxlength="50" class=""/>
		   </td>
		</tr>
		<tr bgcolor="#FFFFFF">
		   <td class="tdLabel"><%=Menus.ALIAS_PARENT_ID%></td>
		   <td>
				<input  type="text" name="s_parentId" size="30" maxlength="21" class="validate-integer "/>
		   </td>
		</tr>
	
	</table>
</s:form>	
			
</body>

</html>