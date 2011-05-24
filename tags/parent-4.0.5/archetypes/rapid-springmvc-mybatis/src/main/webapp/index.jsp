<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>rapid-framework</title>

<style type="text/css">

body {
    font-size:12px;
}

/* 可抽取出 global.css  START*/
html, body, div, span, object, iframe, pre, a, code, font, ul, li, img, samp, small, strong, big, form, label, table, caption, tr, td {
    margin:0px;
	padding:0px;
	border:0px;
	outline:0;
	font-weight:inherit;
	font-style:inherit;
	font-family:inherit;
	text-align:left;
	vertical-align:baseline;
}

.content { 
	clear:both;
	margin:0px auto 0px auto;
	text-align:left;
}
/* 可抽取出 global.css  END*/

.content {
	padding-left: 10px;
}

/* 可抽取出 layouts.css  START*/

/* header start*/
#header {
	width:100%;
	height:60px;
	position:relative;
}
#logo {
    background:url(http://code.google.com/p/rapid-framework/logo?logo_id=1242057585) no-repeat;
	width:141px;
	height:42px;
	position:absolute;
	left:10px;
	top:10px;
}
#main_title {
	font-size: xx-large;
	color: #969D98;
	float: left;
	position:absolute;
	left:10px;
	top:10px;
}

#personal {
    position:absolute;
	right:10px;
	top:20px;
	width:auto;
}
/* header end*/

/* nav start*/
#nav {
    width:100%;
	height:40px;
	line-height:40px;
	background:#FF6D06;
	color:#fff;
	font-size:14px;
	font-weight:bold;
}
#nav li {
    list-style:none;
}
#nav a {
    color:#fff;
	border-right: 1px solid #fff; /* | menu1 | menu2 | border*/
	padding: 0 1em 0 0;
}
#nav ul li {
    float:left;
	padding:0px 5px;
	margin:0px 10px;
}
/* nav end*/

/* main start */
#main {
    width:100%;
}
#left {
	width:190px;
	text-align:left;
	background: repeat-x #E9E9E9;
	vertical-align:top;
	padding-top:15px;
}
#right {
    background:#fff;
	vertical-align:top;
}
/* main end */

/* 可抽取出 layouts.css  END*/

</style>

<script src="scripts/jquery.js"></script>
<script>

/** 自动调整div高度 */
function autoResizeDivFunc() {
	var autoDivHeight = (document.documentElement.clientHeight || document.body.clientHeight) - $('#main').position().top;
	if($.browser.msie) {
		$('#main iframe').height( autoDivHeight);
	}else {
		$('#main').height( autoDivHeight);
	}
};

$(document).ready(function() {
	autoResizeDivFunc();
	$(window).bind('resize',autoResizeDivFunc);
});

</script>
</head>
<body scroll="no">
 <div class="content">

  <div id="header">
   <div id="logo"></div>
   <span id="main_title">
		Rapid-Framework
   </span>
   <div id="personal">当前用户：badqiu | 角色：管理员 | <a href="#">退出</a></div>
  </div>

  <div id="nav">
   <ul>
    <li><a href="left.jsp" target="leftFrame">菜单</a></li>
    <li><a href="../doc/guide.html" target="rightFrame">项目搭建</a></li>
    <li><a href="http://www.google.cn/search?q=%E5%90%8E%E5%8F%B0+%E6%A8%A1%E6%9D%BF" target="rightFrame">Google后台模板</a></li>
    <li><a href="http://www.qq.com" target="leftFrame">修改密码</a></li>
	<li><a href="http://code.google.com/p/rapid-framework/" target="rightFrame">rapid-framework</a></li>
   </ul>
   <div class="end">CSS未提取出来主要是为了你便于删除</div>
  </div>

  <div id="main">
    <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
      <tr height="100%">
        <td align="left" id="left">
          <iframe name="leftFrame" width="100%" height="100%" src="left.html" frameborder="0" scrolling="no"></iframe>
        </td>
        <td align="left" id="right">
          <iframe name="rightFrame" width="100%" height="100%" src="http://code.google.com/p/rapid-framework/wiki/menu" frameborder="0" scrolling="auto"></iframe>
        </td>
      </tr>
    </table>
  </div>
 </div>

</body>
</html>
