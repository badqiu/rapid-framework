<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Error Page</title>
	<script src="<c:url value="/scripts/prototype.js"/>" type="text/javascript"></script>
	<script language="javascript">
		function showDetail()
		{
			var elm = document.getElementById('detail_system_error_msg');
			if(elm.style.display == '') {
				elm.style.display = 'none';
			}else {
				elm.style.display = '';
			}
		}
	</script>
</head>

<body>

<div id="content">
	<%
		//Exception from JSP didn't log yet ,should log it here.
		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
		LogFactory.getLog(requestUri).error(exception.getMessage(), exception);
	%>
	<img alt="error" src="http://www.moozdd.com/theme/image/error_error.gif" />
	<h3>
	对不起,发生系统内部错误,不能处理你的请求<br />
	错误信息: <%=exception.getMessage()%>
	</h3>
	<br>

	<button onclick="history.back();">返回</button>
	<br>

	<p><a href="#" onclick="showDetail();">点击这里查看具体错误消息</a>,报告以下错误消息给系统管理员,可以更加快速的解决问题</p>

	<div id="detail_system_error_msg" style="display:none">
		<pre><%exception.printStackTrace(new java.io.PrintWriter(out));%></pre>
	</div>
</div>
</body>
</html>