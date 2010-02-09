package appcommon.flex
{
	/**
	 * 全局中介者，持有主要的view对象，使各个对象间的耦合松散。
	 * 
	 * 具体说明:
	 *   如command与view之间的多对多关系，通过该对象转为多对1的关系
	 *   即:多个Command全部只需跟该对象交互即可，不需要直接与视图打交道.
	 */
	public class GlobalMediator
	{
		private static var instance:GlobalMediator;
		public function GlobalMediator()
		{
			if (instance != null)
			{
				throw new Error("Only one GlobalMediator instance should be instantiated");
			}
		}
		public static function getInstance():GlobalMediator
		{
			if (instance == null)
			{
				instance = new GlobalMediator();
			}
			return instance;
		}
		
		public function changeMainView(viewName : String)
		{

		}
		
		public function reloadCurrentViewData()
		{

		}		
	}
}