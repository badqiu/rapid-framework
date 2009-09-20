<%@page import="${basepackage}.model.*" %>
<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<%@page import="com.company.project.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

	<input type="hidden" id="userId" name="userId" value="${userInfo.userId}"/>

	<tr>	
		<td class="tdLabel">
			<%=UserInfo.ALIAS_USERNAME%>:
		</td>		
		<td>
		<form:input path="username" id="username" cssClass="" maxlength="50" />
		</td>
	</tr>	
	
	<tr>	
		<td class="tdLabel">
			<%=UserInfo.ALIAS_PASSWORD%>:
		</td>		
		<td>
		<form:input path="password" id="password" cssClass="" maxlength="50" />
		</td>
	</tr>	
	
	<tr>	
		<td class="tdLabel">
			<%=UserInfo.ALIAS_BIRTH_DATE%>:
		</td>		
		<td>
		<input value="${userInfo.birthDateString}" onclick="WdatePicker({dateFmt:'<%=UserInfo.FORMAT_BIRTH_DATE%>'})" id="birthDateString" name="birthDateString"  maxlength="0" class="" />
		</td>
	</tr>	
	
	<tr>	
		<td class="tdLabel">
			<%=UserInfo.ALIAS_SEX%>:
		</td>		
		<td>
		<form:input path="sex" id="sex" cssClass="validate-integer " maxlength="3" />
		</td>
	</tr>	
	
	<tr>	
		<td class="tdLabel">
			<%=UserInfo.ALIAS_AGE%>:
		</td>		
		<td>
		<form:input path="age" id="age" cssClass="validate-integer " maxlength="10" />
		</td>
	</tr>	
	


