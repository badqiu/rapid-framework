package appcommon.flex
{
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
		
		public function reflashCurrentView()
		{

		}		
	}
}