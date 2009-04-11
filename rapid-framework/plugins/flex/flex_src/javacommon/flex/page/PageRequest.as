
 
package javacommon.flex.page{
	

	[Bindable]
	[RemoteClass(alias="cn.org.rapid_framework.page.PageRequest")]
	public class PageRequest
	{
		public function PageRequest(){
			this.pageSize = 10;
			this.pageNumber = 1;
		}
		public var pageSize:Number;
		public var pageNumber:Number;
		public var sortingColumns:String;
		
	}
	
	
}