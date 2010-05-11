
 
package appcommon.flex.page{
	import mx.collections.ArrayCollection;
	
	

	[Bindable]
	[RemoteClass(alias="cn.org.rapid_framework.page.PageRequest")]
	public class PageRequest
	{
		public function PageRequest(pageNumber : Number = 1,filters : Object = null,sortColumns : String = null){
			this.pageSize = DEFAULT_PAGE_SIZE;
			this.pageNumber = pageNumber;
			this.sortingColumns = sortColumns;
			this.filters = filters;
		}
		public var pageSize:Number;
		public var pageNumber:Number;
		public var sortingColumns:String;
		public var filters : Object;
		
		public static var DEFAULT_PAGE_SIZE : Number = 10;
	}
	
	
}