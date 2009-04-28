<%@ include file="/commons/taglibs.jsp" %>

<s:if test="%{actionErrors != null && actionErrors.size > 0}">
	<div class="error">
		<s:iterator value="%{actionErrors}">
			<img src="${ctx}/images/iconWarning.gif" alt="Warning"/><s:property/><br/>
		</s:iterator>
	</div>
</s:if>

<s:if test="%{actionMessages != null && actionMessages.size > 0}">	
	<div class="message">
		<s:iterator value="%{actionMessages}">
			<img src="${ctx}/images/iconInformation.gif" alt="Info"/><s:property/><br/>
		</s:iterator>
	</div>
</s:if>	
