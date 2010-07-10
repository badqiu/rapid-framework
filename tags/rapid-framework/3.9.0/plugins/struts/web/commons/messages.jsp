<%-- Error Messages --%>
<%@ include file="/commons/taglibs.jsp" %>
<logic:messagesPresent>
	<div class="flash_error">
		<html:messages id="error">
			${error}<br/>
		</html:messages>
	</div>
</logic:messagesPresent>

<%-- Success Messages --%>
<logic:messagesPresent message="true">
	<div class="flash_success">
		<html:messages id="message" message="true">
			${message}<br/>
		</html:messages>
	</div>
</logic:messagesPresent>




