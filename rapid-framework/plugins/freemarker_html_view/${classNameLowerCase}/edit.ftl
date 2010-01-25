<#include "/commons/macro.ftl" />
<@override name="haed">
	<title><%=UserInfo.TABLE_ALIAS%>编辑</title>
</@override>

<@override name="content">

<form method="post" action="${ctx}/userinfo/${userInfo.userId}">
	<input type="hidden" name="_method" value="put"/>
	
	<input id="submitButton" name="submitButton" type="submit" value="提交" />
	<input type="button" value="返回列表" onclick="window.location='${ctx}/userinfo'"/>
	<input type="button" value="后退" onclick="history.back();"/>
	
	<table class="formTable">
	<#include "form_include.ftl" />
	</table>
</form>

<script>
	
	new Validation(document.forms[0],{onSubmit:true,onFormValidate : function(result,form) {
		var finalResult = result;
		
		//在这里添加自定义验证
		
		return disableSubmit(finalResult,'submitButton');
	}});
</script>
</@override>

<@extends name="/base.ftl"/>