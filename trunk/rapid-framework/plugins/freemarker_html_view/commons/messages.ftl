<%-- Error Messages --%>
<#if (springMessages != null && springMessages?size > 0 )>
	<div class="message">
        <#list springMessages as msg>
			<img src="${ctx}/images/iconInformation.gif" alt="Info"/>${msg}<br/>
		</#list>
	</div>    
</#if>

<%-- Info Messages --%>
<#if (springErrors != null && springErrors?size > 0 )>
	<div class="error">
        <#list springErrors as errorMsg>
			<img src="${ctx}/images/iconWarning.gif" alt="Warning"/>${errorMsg}<br/>
        </#list>
	</div>    
</#if>