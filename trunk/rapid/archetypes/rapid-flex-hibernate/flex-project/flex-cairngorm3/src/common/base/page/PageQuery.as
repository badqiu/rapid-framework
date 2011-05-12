
/**
 *@author hunhun
 */
package common.base.page
{

	[Bindable]
	[RemoteClass(alias="cn.org.rapid_framework.util.page.PageQuery")]
	public class PageQuery
	{
		public var pageSize:Number=DEFAULT_PAGE_SIZE;
		public var page:Number;

		public static const DEFAULT_PAGE_SIZE:Number=10;

		public function PageQuery()
		{

		}




	}


}

