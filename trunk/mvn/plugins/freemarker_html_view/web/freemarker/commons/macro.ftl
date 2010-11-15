<#assign ctx='../'/>
<#assign URL_PREFIX='../'/>
<#macro pageToolBar page pageSizeSelectList=[10,20,30] isShowPageSizeList=true>
<table width="100%"  border="0" cellspacing="0" class="gridToolbar">
  <tr>
	<td>
	<div class="box">
		
		<div  class="leftControls" >
			<#nested />
		</div>
		
		<div class="paginationControls">
			<span class="buttonLabel">${page.thisPageFirstElementNumber} - ${page.thisPageLastElementNumber} of ${page.totalCount}</span>
			
			<#if page.firstPage>
			<img src="${URL_PREFIX}/widgets/simpletable/images/firstPageDisabled.gif" style="border:0" >
			<#else>
			<a href="javascript:simpleTable.togglePage(1);"><img src="${URL_PREFIX}/widgets/simpletable/images/firstPage.gif" style="border:0" ></a>
			</#if>
			
			<#if page.hasPreviousPage>
			<a href="javascript:simpleTable.togglePage(${page.previousPageNumber});"><img src="${URL_PREFIX}/widgets/simpletable/images/prevPage.gif" style="border:0" ></a>
			<#else>
			<img src="${URL_PREFIX}/widgets/simpletable/images/prevPageDisabled.gif" style="border:0" >
			</#if>
			
			<#list page.linkPageNumbers as item>
				<#if item == page.thisPageNumber>
					[${item}]
				<#else>
					<a href="javascript:simpleTable.togglePage(${item});">[${item}]</a>
				</#if>
			</#list>

			<#if page.hasNextPage>				
			<a href="javascript:simpleTable.togglePage(${page.nextPageNumber});"><img src="${URL_PREFIX}/widgets/simpletable/images/nextPage.gif" style="border:0" ></a>
			<#else>
			<img src="${URL_PREFIX}/widgets/simpletable/images/nextPageDisabled.gif" style="border:0" >
			</#if>
			
			<#if page.lastPage>
			<img src="${URL_PREFIX}/widgets/simpletable/images/lastPageDisabled.gif" style="border:0">
			<#else>
			<a href="javascript:simpleTable.togglePage(${page.lastPageNumber});"><img src="${URL_PREFIX}/widgets/simpletable/images/lastPage.gif" style="border:0" ></a>
			</#if>
			
			<#if isShowPageSizeList>
			<select onChange="simpleTable.togglePageSize(this.value)">
				<#list pageSizeSelectList as item>
					<#if page.pageSize == item>
						<option value="${item}" selected>${item}</option>
					<#else>
						<option value="${item}">${item}</option>
					</#if>
				</#list>
			</select>
			</#if>
		</div>
	<div>
	</td>
  </tr>
</table>
</#macro>
