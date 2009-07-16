<%@page import="com.awd.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<input type="hidden" name="id" value="${id}" />
<table class="inputView">
	<tr>
		<td>登录名:</td>
		<td><input type="text" name="loginName" size="40" id="loginName" value="${loginName}" /></td>
	</tr>
	<tr>
		<td>用户名:</td>
		<td><input type="text" name="name" size="40" value="${name}" /></td>
	</tr>
	<tr>
		<td>密码:</td>
		<td><input type="password" id="password" name="password" size="40" value="${password}" /></td>
	</tr>
	<tr>
		<td>确认密码:</td>
		<td><input type="password" name="passwordConfirm" size="40" value="${password}" /></td>
	</tr>
	<tr>
		<td>邮箱:</td>
		<td><input type="text" name="email" size="40" value="${email}" /></td>
	</tr>
	<tr>
		<td>角色:</td>
		<td>
			<div style="word-break:break-all;width:250px; overflow:auto; ">
				<s:checkboxlist name="checkedRoleIds"  list="allRoles"  listKey="id" listValue="name" theme="simple"/>
			</div>
		</td>
	</tr>
		<tr>
		<td>菜单:</td>
		<td>
			<div style="word-break:break-all;width:250px; overflow:auto; ">
				<s:checkboxlist name="checkedMenuIds"  list="allMenus"  listKey="id" listValue="name" theme="simple"/>
			</div>
		</td>
	</tr>
					
	<tr>
		<td colspan="2">
			<input type="submit" value="提交" />&nbsp; 
			<input type="button" value="取消" onclick="history.back()"/>
		</td>
	</tr>
</table>
	
