<%@ page contentType="text/html;charset=UTF-8" %>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>

<head>
	<%@ include file="/commons/meta.jsp" %>
	<base href="<%=basePath%>">
	<title>登陆</title>
<style type="text/css">            

</style> 	
</head>

<body>
<%@ include file="/commons/messages.jsp" %>

<form action="${ctx}/demo/User/login.do" method="post" >
	<table height="100%" width="100%" >
	<tr valign="middle">
		<td>
			<table align="center">
				<tr>	
					<th>用户名&nbsp;</th>	
					<td>
					<input name="username" class="required"/>
					</td>
				</tr>
				
				<tr>	
					<th>密码&nbsp;</th>	
					<td>
					<input type="password" name="password" class="required"/>
					</td>
				</tr>
				
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="登陆" />
					</td>
				</tr>		
			</table>
		</td>
	</tr>
	</table>
</form>

<script>
	new Validation(document.forms[0],{onSubmit:true});
</script>

</body>
</html>
