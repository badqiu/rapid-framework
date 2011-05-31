<%@page import="com.company.project.domain.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

	<input type="hidden" id="userId" name="userId" value="${userInfo.userId}"/>

	<tr>	
		<td class="tdLabel">
			<span class="required">*</span><%=UserInfo.ALIAS_USERNAME%>:
		</td>		
		<td>
		<form:input path="username" id="username" cssClass="required " maxlength="50" />
		<font color='red'><form:errors path="username"/></font>
		</td>
	</tr>	
	
	<tr>	
		<td class="tdLabel">
			<%=UserInfo.ALIAS_PASSWORD%>:
		</td>		
		<td>
		<form:input path="password" id="password" cssClass="" maxlength="50" />
		<font color='red'><form:errors path="password"/></font>
		</td>
	</tr>	
	
	<tr>	
		<td class="tdLabel">
			<%=UserInfo.ALIAS_BIRTH_DATE%>:
		</td>		
		<td>
		<input value="${userInfo.birthDateString}" onclick="WdatePicker({dateFmt:'<%=UserInfo.FORMAT_BIRTH_DATE%>'})" id="birthDateString" name="birthDateString"  maxlength="0" class="" />
		<font color='red'><form:errors path="birthDate"/></font>
		</td>
	</tr>	
	
	<tr>	
		<td class="tdLabel">
			<%=UserInfo.ALIAS_SEX%>:
		</td>		
		<td>
		<form:input path="sex" id="sex" cssClass="validate-integer max-value-2147483647" maxlength="10" />
		<font color='red'><form:errors path="sex"/></font>
		</td>
	</tr>	
	
	<tr>	
		<td class="tdLabel">
			<%=UserInfo.ALIAS_AGE%>:
		</td>		
		<td>
		<form:input path="age" id="age" cssClass="validate-integer max-value-2147483647" maxlength="10" />
		<font color='red'><form:errors path="age"/></font>
		</td>
	</tr>	
	
		