<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

<%-- jsp模板继承,具体使用请查看: http://code.google.com/p/rapid-framework/wiki/rapid_jsp_extends --%>

<html>

<head>
	<%@ include file="/commons/meta.jsp" %>
	<html:base/>
	<rapid:block name="head"/>
</head>
<body>
	<%@ include file="/commons/messages.jsp" %>

	<rapid:block name="content"/>
	
</body>
</html>	