package appcommon.flex
{
	import appcommon.flex.base.HashMap;
	
	[Bindable]
	public class GlobalMap extends HashMap
	{
		
		private static var instance:GlobalMap;
		
		public function GlobalMap()
		{
			super();
			if (instance != null)
			{
				throw new Error("GlobalMap is singleton");
			}
		}
		
		public static function getInstance():GlobalMap
		{
			if (instance == null)
				instance=new GlobalMap();
			
			return instance;
		}
	}
}