package appcommon.flex
{
	/**
	 * 全局中介者，持有主要的view对象，使各个对象间的耦合松散。
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