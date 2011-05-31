<%@page import="com.company.project.domain.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

<rapid:override name="head">
	<title><%=UserInfo.TABLE_ALIAS%>信息</title>
</rapid:override>

<rapid:override name="content">
	<form:form modelAttribute="userinfo"  >
		<input type="button" value="返回列表" onclick="window.location='${ctx}/userinfo'"/>
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
</rapid:override>

<%-- jsp模板继承,具体使用请查看: http://code.google.com/p/rapid-framework/wiki/rapid_jsp_extends --%>
<%@ include file="base.jsp" %>