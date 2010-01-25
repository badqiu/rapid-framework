<#include "/commons/macro.ftl" />

<@override name="head">
	<title><%=UserInfo.TABLE_ALIAS%>信息</title>
</@override>

<@override name="content">
<form>
	<input type="button" value="返回列表" onclick="window.location='${ctx}/userinfo'"/>
	<input type="button" value="后退" onclick="history.back();"/>
	
	<input type="hidden" id="userId" name="userId" value="${userInfo.userId!}"/>

	<table class="formTable">
		<tr>	
			<td class="tdLabel">UserInfo.ALIAS_USERNAME</td>	
			<td>${userInfo.username!}</td>
		</tr>
		<tr>	
			<td class="tdLabel">UserInfo.ALIAS_PASSWORD</td>	
			<td>${userInfo.password!}</td>
		</tr>
		<tr>	
			<td class="tdLabel">UserInfo.ALIAS_BIRTH_DATE</td>	
			<td>${userInfo.birthDateString!}</td>
		</tr>
		<tr>	
			<td class="tdLabel">UserInfo.ALIAS_SEX</td>	
			<td>${userInfo.sex!}</td>
		</tr>
		<tr>	
			<td class="tdLabel">UserInfo.ALIAS_AGE</td>	
			<td>${userInfo.age!}</td>
		</tr>
	</table>
</form>
</@override>

<@extends name="/base.ftl"/>