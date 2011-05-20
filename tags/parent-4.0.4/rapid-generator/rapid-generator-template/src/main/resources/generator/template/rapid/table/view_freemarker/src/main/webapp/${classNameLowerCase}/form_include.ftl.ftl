<#include "/macro.include"/>
<#include "/custom.include"/>  
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first> 
<#assign classNameLowerCase = className?lower_case> 

<#list table.columns as column>
<#if column.htmlHidden>
	<input type="hidden" id="${column.columnNameLower}" name="${column.columnNameLower}" value="<@jspEl classNameFirstLower+"."+table.idColumn.columnNameFirstLower+"!"/>"/>
</#if>
</#list>

<#list table.columns as column>
	<#if !column.htmlHidden>	
	<tr>	
		<td class="tdLabel">
			<#if !column.nullable><span class="required">*</span></#if>${className}.ALIAS_${column.constantName}:
		</td>		
		<td>
	<#if column.isDateTimeColumn>
		<input value="<@jspEl classNameFirstLower+"."+column.columnNameLower+"String!"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" id="${column.columnNameLower}String" name="${column.columnNameLower}String"  maxlength="0" class="${column.validateString}" />
	<#else>
		<input value="<@jspEl classNameFirstLower+"."+column.columnNameLower+"!"/>" name="${column.columnNameLower}" id="${column.columnNameLower}" class="${column.validateString}" maxlength="${column.size}" />
	</#if>
		</td>
	</tr>	
	
	</#if>
</#list>
