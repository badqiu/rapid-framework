<%@page import="com.company.project.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

	<s:hidden id="userId" name="userId" />

<!-- ONGL access static field: @package.class@field or @vs@field -->
	
	<s:textfield label="%{@com.company.project.model.UserInfo@ALIAS_USERNAME}" key="username" value="%{model.username}" cssClass="" required="false" />
	
	
	<s:textfield label="%{@com.company.project.model.UserInfo@ALIAS_PASSWORD}" key="password" value="%{model.password}" cssClass="" required="false" />
	
	
	<tr>	
		<td class="tdLabel">
			<%=UserInfo.ALIAS_BIRTH_DATE%>:
		</td>	
		<td>
		<input value="${model.birthDateString}" onclick="WdatePicker({dateFmt:'<%=UserInfo.FORMAT_BIRTH_DATE%>'})" id="birthDateString" name="birthDateString"  maxlength="0" class="" />
		</td>
	</tr>
	
	
	<s:textfield label="%{@com.company.project.model.UserInfo@ALIAS_SEX}" key="sex" value="%{model.sex}" cssClass="validate-integer max-value-2147483647" required="false" />
	
	
	<s:textfield label="%{@com.company.project.model.UserInfo@ALIAS_AGE}" key="age" value="%{model.age}" cssClass="validate-integer max-value-2147483647" required="false" />
	
