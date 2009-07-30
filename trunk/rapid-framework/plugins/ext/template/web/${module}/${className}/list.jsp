<%@page import="${basepackage}.model.*" %>
<#include "macro.include"/> 
<#include "custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>
<html>

<head>
	<%@ include file="/commons/meta.jsp" %>
	<html:base/>
	
	<link type="text/css" rel="stylesheet" href="<@jspEl 'ctx'/>/scripts/ext2/resources/css/ext-all.css"/>
	<link type="text/css" rel="stylesheet" href="<@jspEl 'ctx'/>/styles/ext2List.css"/>
	
	<!--Ext 类库  -->
    <script type="text/javascript" src="<@jspEl 'ctx'/>/scripts/ext2/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="<@jspEl 'ctx'/>/scripts/ext2/ext-all.js"/></script>
	<script type="text/javascript" src="<@jspEl 'ctx'/>/scripts/ext2/source/locale/ext-lang-zh_CN.js"></script>
	
	<!-- DWR 类库 -->
	<script type="text/javascript" src="<@jspEl 'ctx'/>/dwr/engine.js"></script>  
	<script type="text/javascript" src="<@jspEl 'ctx'/>/dwr/util.js"></script>
    <script type='text/javascript' src="<@jspEl 'ctx'/>/dwr/interface/${className}Manager.js"></script>
    
	<!-- 本页功能代码 -->
    <script type='text/javascript' src='list.js'></script>
    
	<title><%=${className}.TABLE_ALIAS%> 维护</title>
</head>

<body>
	<div id="editor-grid"></div>
</body>
</html>

