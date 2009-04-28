<%-- Error Messages --%>
<%@ include file="/commons/taglibs.jsp" %>
<logic:messagesPresent>
	<div class="error">
		<html:messages id="error">
			<img src="${ctx}/images/iconWarning.gif" alt="Warning"/>${error}<br/>
		</html:messages>
	</div>
</logic:messagesPresent>

<%-- Success Messages --%>
<logic:messagesPresent message="true">
	<div class="message" id="__messageInfo">
		<html:messages id="message" message="true">
			<img src="${ctx}/images/iconInformation.gif" alt="Info"/>${message}<br/>
		</html:messages>
	</div>
	<script type="text/javascript">
		setTimeout(function(){
			document.getElementById('__messageInfo').style.display = 'none';
		},8000)
	</script>
</logic:messagesPresent>
