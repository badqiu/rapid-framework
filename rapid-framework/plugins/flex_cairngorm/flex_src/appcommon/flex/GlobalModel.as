package appcommon.flex
{

	import mx.collections.ArrayCollection;
	

	[Bindable]
	public class GlobalModel
	{

		private static var instance:GlobalModel;


		public function GlobalModel()
		{
			if (instance != null)
			{
				throw new Error("Only one GlobalModel instance should be instantiated");
			}
		}

		public static function getInstance():GlobalModel
		{
			if (instance == null)
			{
				instance = new GlobalModel();
			}
			return instance;
		}


	}
}