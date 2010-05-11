<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first> 
<#assign classNameLowerCase = className?lower_case> 
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>

<head>
	<%@ include file="/commons/meta.jsp" %>
	<base href="<%=basePath%>">
	<title><%=${className}.TABLE_ALIAS%>新增</title>
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<form:form method="post" action="<@jspEl "ctx"/>/${classNameLowerCase}" modelAttribute="${classNameFirstLower}" >
	<input id="submitButton" name="submitButton" type="submit" value="提交" />
	<input type="button" value="返回列表" onclick="window.location='<@jspEl "ctx"/>/${classNameLowerCase}'"/>
	<input type="button" value="后退" onclick="history.back();"/>
	
	<table class="formTable">
	<%@ include file="form_include.jsp" %>
	</table>
</form:form>

<script>
	
	new Validation(document.forms[0],{onSubmit:true,onFormValidate : function(result,form) {
		var finalResult = result;
		
		//在这里添加自定义验证
		
		return disableSubmit(finalResult,'submitButton');
	}});
</script>

</body>
</html>
