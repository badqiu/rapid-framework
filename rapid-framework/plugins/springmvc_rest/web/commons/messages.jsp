<%@ include file="/commons/taglibs.jsp" %>

<%-- Error Messages --%>
<c:if test="${flash.success != null}">
	<div class="flash_success">
		${flash.success}<br/>
	</div>    
</c:if>

<%-- Info Messages --%>
<c:if test="${flash.error != null}">
	<div class="flash_error">
		${flash.error}<br/>
	</div> 
</c:if>
