<%@page import="com.awd.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

	<s:hidden id="id" name="id" ></s:hidden>

<!-- ONGL access static field: @package.class@field or @vs@field -->
	
	<s:textfield label="%{@vs@ALIAS_USERNAME}" key="username" value="%{model.username}" cssClass="" required="false"></s:textfield>
	
	
	<s:textfield label="%{@vs@ALIAS_USERPHONE}" key="userphone" value="%{model.userphone}" cssClass="" required="false"></s:textfield>
	
