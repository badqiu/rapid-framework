<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
<?xml version='1.0' encoding='UTF-8'?>
<!-- DBUnit flatXml DataFile -->
<dataset>

	<#assign testdatas = gg.executeSql("select * from "+table.sqlName,5) />
	<#if (testdatas?size > 0)>
		<#list testdatas as map >
		<${table.sqlName}
			<#list map?keys?chunk(5) as row>
				<#list row as key> 
				${key}='${map[key]!}' 
				</#list>
			</#list>
		/>
		
		</#list>	
	<#else>
	  <${table.sqlName} 
		<#list table.columns as column>
			${column.sqlName}='${column.testData}' 
		</#list>  
	  />	
	</#if>
  
</dataset>
