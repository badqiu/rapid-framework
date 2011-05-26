<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="org.apache.struts2.components.Include"%>
<%@ include file="/commons/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/commons/meta.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>menu</title>

<link href="styles/left.css" type="text/css" rel="stylesheet" />
<script src="scripts/jquery.js"></script>
<script>
	$(document).ready(function() {
		$('.urbangreymenu li a').click(function() {
			$('.urbangreymenu li a').removeClass('currentClickMenu');
			$(this).addClass('currentClickMenu');
		});
		
		$('.headerbar').click(function() {
			$(this).next().toggle();
		});
	});
</script>


</head>

<body>

<div class="urbangreymenu">
	
	  <h3 class="headerbar">rapid-framework</h3>
	  <ul>
		<li><a href="http://code.google.com/p/rapid-framework/" target="rightFrame">index</a></li>
		<li><a href="http://code.google.com/p/rapid-framework/downloads/list" target="rightFrame">downloads</a></li>
		<li><a href="http://code.google.com/p/rapid-framework/wiki/menu?tm=6" target="rightFrame">wiki</a></li>
		<li><a href="http://code.google.com/p/rapid-framework/issues/list" target="rightFrame">issues</a></li>
		<li><a href="http://code.google.com/p/rapid-framework/source/checkout" target="rightFrame">source</a></li>
	  </ul>

	  <h3 class="headerbar">网页链接</h3>
	  <ul>
		<li><a href="http://www.rapid-framework.org.cn" target="rightFrame">rapid-frameworl.org.cn</a></li>
	  </ul>
	  
	  <h3 class="headerbar">删除说明</h3>
	  <ul>
		<li>不喜欢本界面,请删除如下相关资源</li>
		<li>index.html</li>
		<li>left.html</li>
		<li>styles/images/left</li>
		<li>styles/left.css</li>
	  </ul>

</div>

</body>
</html>
