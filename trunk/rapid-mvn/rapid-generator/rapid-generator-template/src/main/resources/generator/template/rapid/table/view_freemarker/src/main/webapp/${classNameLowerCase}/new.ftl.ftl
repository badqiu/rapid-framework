<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first> 
<#assign classNameLowerCase = className?lower_case> 

<#noparse><#include "/commons/macro.ftl" /></#noparse>

<#noparse><@override name="head"></#noparse>
	<title>${className}.TABLE_ALIAS 新增</title>
<#noparse></@override></#noparse>

<#noparse><@override name="content"></#noparse>
	<form method="post" action="<@jspEl 'ctx'/>/${classNameLowerCase}" >
		<input id="submitButton" name="submitButton" type="submit" value="提交" />
		<input type="button" value="返回列表" onclick="window.location='<@jspEl 'ctx'/>/${classNameLowerCase}'"/>
		<input type="button" value="后退" onclick="history.back();"/>
		
		<table class="formTable">
		<#noparse><#include "form_include.ftl" /></#noparse>
		</table>
	</form>
	
	<script>
		
		new Validation(document.forms[0],{onSubmit:true,onFormValidate : function(result,form) {
			var finalResult = result;
			
			//在这里添加自定义验证
			
			return disableSubmit(finalResult,'submitButton');
		}});
	</script>
<#noparse></@override></#noparse>

<#-- freemarker模板继承,具体使用请查看: http://code.google.com/p/rapid-framework/wiki/rapid_freemarker_extends -->
<#noparse><@extends name="*/base.ftl"/></#noparse>