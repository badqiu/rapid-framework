<%@page import="com.awd.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

	<s:hidden id="id" name="id" ></s:hidden>

<!-- ONGL access static field: @package.class@field or @vs@field -->
	
	<s:textfield label="%{@vs@ALIAS_NAME}" key="name" value="%{model.name}" cssClass="required " required="true"></s:textfield>
	
	
	<s:textfield label="%{@vs@ALIAS_DISPLAY_NAME}" key="displayName" value="%{model.displayName}" cssClass="required " required="true"></s:textfield>
	
