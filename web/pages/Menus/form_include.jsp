<%@page import="com.awd.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

	<s:hidden id="id" name="id" ></s:hidden>

<!-- ONGL access static field: @package.class@field or @vs@field -->
	
	<s:textfield label="%{@vs@ALIAS_JSBH}" key="jsbh" value="%{model.jsbh}" cssClass="validate-integer " required="false"></s:textfield>
	
	
	<s:textfield label="%{@vs@ALIAS_DESCN}" key="descn" value="%{model.descn}" cssClass="" required="false"></s:textfield>
	
	
	<s:textfield label="%{@vs@ALIAS_URL}" key="url" value="%{model.url}" cssClass="" required="false"></s:textfield>
	
	
	<s:textfield label="%{@vs@ALIAS_IMAGE}" key="image" value="%{model.image}" cssClass="" required="false"></s:textfield>
	
	
	<s:textfield label="%{@vs@ALIAS_NAME}" key="name" value="%{model.name}" cssClass="" required="false"></s:textfield>
	
	
	<s:textfield label="%{@vs@ALIAS_THE_SORT}" key="theSort" value="%{model.theSort}" cssClass="validate-integer " required="false"></s:textfield>
	
	
	<s:textfield label="%{@vs@ALIAS_QTIP}" key="qtip" value="%{model.qtip}" cssClass="" required="false"></s:textfield>
	
	
	<s:textfield label="%{@vs@ALIAS_PARENT_ID}" key="parentId" value="%{model.parentId}" cssClass="validate-integer " required="false"></s:textfield>
	
