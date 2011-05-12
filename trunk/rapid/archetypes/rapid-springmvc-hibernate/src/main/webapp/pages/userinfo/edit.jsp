<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

<rapid:override name="head">
	<title><%=UserInfo.TABLE_ALIAS%>编辑</title>
</rapid:override>

<rapid:override name="content">
	<form:form method="put" action="${ctx}/userinfo/${userInfo.userId}" modelAttribute="userInfo">
		<input id="submitButton" name="submitButton" type="submit" value="提交" />
		<input type="button" value="返回列表" onclick="window.location='${ctx}/userinfo'"/>
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
</rapid:override>

<%-- jsp模板继承,具体使用请查看: http://code.google.com/p/rapid-framework/wiki/rapid_jsp_extends --%>
<%@ include file="base.jsp" %>