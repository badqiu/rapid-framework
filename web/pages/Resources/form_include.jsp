<%@page import="com.awd.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

	<s:hidden id="id" name="id" ></s:hidden>

<!-- ONGL access static field: @package.class@field or @vs@field -->
	
	<s:textfield label="%{@vs@ALIAS_RESOURCE_TYPE}" key="resourceType" value="%{model.resourceType}" cssClass="required " required="true"></s:textfield>
	
	
	<s:textfield label="%{@vs@ALIAS_VALUE}" key="value" value="%{model.value}" cssClass="required " required="true"></s:textfield>
	
	
	<s:textfield label="%{@vs@ALIAS_ORDER_NUM}" key="orderNum" value="%{model.orderNum}" cssClass="required validate-integer " required="true"></s:textfield>
	
