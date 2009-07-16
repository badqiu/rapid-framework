<%@page import="com.awd.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

<!-- ONGL access static field: @package.class@field or @vs@field -->
	<input type="hidden" name="id" value="${id}" />
<table>
	<tr>
		<td>角色名:</td>
		<td><input type="text" name="name" size="40" value="${name}" class="required"/></td>
	</tr>
	<tr>
		<td>授权:</td>
		<td>
			<div style="word-break:break-all;width:300px; overflow:auto; ">
				<s:checkboxlist name="checkedAuthIds"  list="allAuths"  listKey="id" listValue="displayName" theme="simple"/>
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