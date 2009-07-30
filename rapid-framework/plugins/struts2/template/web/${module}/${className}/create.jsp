<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<#assign actionExtension = "do"> 

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>

<head>
	<%@ include file="/commons/meta.jsp" %>
	<base href="<%=basePath%>">
	<title><%=${className}.TABLE_ALIAS%>新增</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<s:form action="${strutsActionBasePath}/save.${actionExtension}" method="post">
	<input id="submit" name="submit" type="submit" value="提交" />
	<input type="button" value="返回列表" onclick="window.location='<@jspEl 'ctx'/>${strutsActionBasePath}/list.${actionExtension}'"/>
	
	<table class="formTable">
	<%@ include file="form_include.jsp" %>
	</table>
</s:form>

<script>
	
	new Validation(document.forms[0],{onSubmit:true,onFormValidate : function(result,form) {
		var finalResult = result;
		
		//在这里添加自定义验证
		
		return disableSubmit(finalResult,'submit');
	}});
</script>

</body>
</html>