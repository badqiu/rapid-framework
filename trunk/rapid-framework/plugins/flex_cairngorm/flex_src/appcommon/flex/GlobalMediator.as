package appcommon.flex
{
	import mx.containers.ViewStack;
	import mx.core.UIComponent;
	
	/**
	 * 全局中介者，持有主要的view对象，使各个对象间的耦合松散。
	 * 
	 * 具体说明:
	 *   如command与view之间的多对多关系，通过该对象转为多对1的关系
	 *   即:多个Command全部只需跟该对象交互即可，不需要直接与视图打交道.
	 */
	public class GlobalMediator
	{
		public static var worksapce : ViewStack;
		public static var menu : ViewStack;
		public static var top : UIComponent;
				
		public static function changeMainView(viewName : String)
		{

		}
		
		public static function reloadCurrentViewData()
		{

		}		
	}
}