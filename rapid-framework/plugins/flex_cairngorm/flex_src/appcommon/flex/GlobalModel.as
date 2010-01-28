package com.pomer.common
{

	import mx.collections.ArrayCollection;
	
	


	[Bindable]
	public class GlobalModel
	{

		private static var globalModel:GlobalModel;


		public function GlobalModel()
		{
			if (globalModel != null)
			{
				throw new Error("Only one GlobalModel instance should be instantiated");
			}
		}

		public static function getInstance():GlobalModel
		{
			if (globalModel == null)
			{
				globalModel=new GlobalModel();
			}
			return globalModel;
		}


	}
}