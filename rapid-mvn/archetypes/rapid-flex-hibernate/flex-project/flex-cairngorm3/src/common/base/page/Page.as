
/**
 *@author badqiu
 *@author hunhun
 */
package common.base.page
{

	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="cn.org.rapid_framework.page.Page")]
	public class Page
	{
		public var result:ArrayCollection;
		public var pageSize:int;
		public var thisPageNumber:int;
		public var totalCount:int;

		public var nextPageNumber:int;
		public var previousPageNumber:int;

		public var thisPageLastElementNumber:int;
		public var thisPageFirstElementNumber:int;

		public var lastPageNumber:int;
		public var firstResult:int;


		public var linkPageNumbers:Array;

		public var hasPreviousPage:Boolean;
		public var hasNextPage:Boolean;
		public var firstPage:Boolean;
		public var lastPage:Boolean;

	}


}

