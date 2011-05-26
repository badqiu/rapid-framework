
/**
 *@author badqiu
 *@author hunhun
 */
package common.base
{

	[Bindable]
	public class BaseEntity
	{

		public function BaseEntity()
		{
		}
		//selected属性为了方便检查DataGrid组件上选中的多条记录并进行操作
		[Transient]
		public var selected:Boolean=false;

	}

}
