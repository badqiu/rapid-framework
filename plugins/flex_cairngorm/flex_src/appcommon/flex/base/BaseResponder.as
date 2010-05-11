package appcommon.flex.base
{
	import mx.controls.Alert;
	import mx.rpc.IResponder;

	public class BaseResponder implements IResponder
	{
		public function BaseResponder()
		{
		}

		public function result(data:Object):void
		{
			Alert.show("Not Yet Implement");
		}
		
		public function fault(info:Object):void
		{
			Alert.show(info.toString(),"Server Response Exception");
		}
		
	}
}