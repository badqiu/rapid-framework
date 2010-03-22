<%@ include file="/commons/taglibs.jsp" %>


<%-- Info Messages --%>
<c:if test="${not empty springMessages}">
	<div class="message">
        <c:forEach var="item" items="${springMessages}">
			<img src="${ctx}/images/iconInformation.gif" alt="Info"/>${item}<br/>
        </c:forEach>
	</div>    
</c:if>

<%-- Error Messages --%>
<c:if test="${not empty springErrors}">
	<div class="error">
        <c:forEach var="item" items="${springErrors}">
			<img src="${ctx}/images/iconWarning.gif" alt="Warning"/>${item}<br/>
        </c:forEach>
	</div>    
</c:if>



