
 
package javacommon.flex.page{
	
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="cn.org.rapid_framework.page.Page")]
	public class Page
	{
		public var thisPageNumber : Number;
		public var pageSize : Number;
		public var totalCount : Number;
		
		public var nextPageNumber : Number;
		public var previousPageNumber : Number;
		
		public var thisPageLastElementNumber : Number;
		public var thisPageFirstElementNumber : Number;
		
		public var lastPageNumber : Number;
		public var firstResult : Number;

		public var result : ArrayCollection; 
		public var linkPageNumbers : ArrayCollection;
				
		public var hasPreviousPage : Boolean;
		public var hasNextPage : Boolean;
		public var firstPage : Boolean;
		public var lastPage : Boolean;	
		
	}
	
	
}