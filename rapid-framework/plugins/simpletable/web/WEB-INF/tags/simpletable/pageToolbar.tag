<%@ attribute name="page" required="true" type="cn.org.rapid_framework.page.Page" description="Page.java" %>
<%@ attribute name="pageSizeSelectList" type="java.lang.Number[]" required="true"  %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  
<table width="100%"  border="0" cellspacing="0" class="gridToolbar">
  <tr>
	<td colspan="3">
		<div class="box">
		
			<div  class="leftControls" >
				<jsp:doBody/>
			</div>
			
			<div class="paginationControls">
				<span class="buttonLabel">${page.thisPageFirstElementNumber} - ${page.thisPageLastElementNumber} of ${page.totalCount}</span>
				
				<c:choose>
				<c:when test="${page.firstPage}"><img src="<c:url value='/widgets/simpletable/images/firstPageDisabled.gif'/>" style="border:0" ></c:when>
				<c:otherwise><a href="javascript:simpleTable.togglePage(1);"><img src="<c:url value='/widgets/simpletable/images/firstPage.gif'/>" style="border:0" ></a></c:otherwise>
				</c:choose>
				
				<c:choose>
				<c:when test="${page.hasPreviousPage}"><a href="javascript:simpleTable.togglePage(${page.previousPageNumber});"><img src="<c:url value='/widgets/simpletable/images/prevPage.gif'/>" style="border:0" ></a></c:when>
				<c:otherwise><img src="<c:url value='/widgets/simpletable/images/prevPageDisabled.gif'/>" style="border:0" ></c:otherwise>
				</c:choose>
				
				<c:forEach var="item" items="${page.linkPageNumbers}" varStatus="status">
				<c:choose>
				<c:when test="${item == page.thisPageNumber}">[${item}]</c:when>
				<c:otherwise><a href="javascript:simpleTable.togglePage(${item});">[${item}]</a></c:otherwise>
				</c:choose>
				</c:forEach>
				
				<c:choose>
				<c:when test="${page.hasNextPage}"><a href="javascript:simpleTable.togglePage(${page.nextPageNumber});"><img src="<c:url value='/widgets/simpletable/images/nextPage.gif'/>" style="border:0" ></a></c:when>
				<c:otherwise><img src="<c:url value='/widgets/simpletable/images/nextPageDisabled.gif'/>" style="border:0" ></c:otherwise>
				</c:choose>
				
				<c:choose>
				<c:when test="${page.lastPage}"><img src="<c:url value='/widgets/simpletable/images/lastPageDisabled.gif'/>" style="border:0"></c:when>
				<c:otherwise><a href="javascript:simpleTable.togglePage(${page.lastPageNumber});"><img src="<c:url value='/widgets/simpletable/images/lastPage.gif'/>" style="border:0" ></a></c:otherwise>
				</c:choose>
				
				<select onChange="simpleTable.togglePageSize(this.value)">
					<c:forEach var="item" items="${pageSizeSelectList}">
						<option value="${item}" ${page.pageSize == item ? 'selected' : '' }>${item}</option>
					</c:forEach> 
				</select>
			</div>
		<div>
	</td>
  </tr>
</table>