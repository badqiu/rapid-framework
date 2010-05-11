package appcommon.flex.util
{
	import appcommon.flex.event.CairngormCallbackEvent;
	
	import mx.rpc.IResponder;
	import mx.rpc.events.FaultEvent;
	
	public class ResponderUtils
	{
		public function ResponderUtils()
		{
		}

		public static function notifyCallback(callbackEvent:CairngormCallbackEvent,result:Object) : void
		{
			var callback:IResponder = IResponder(callbackEvent.callback);
			if(callback != null) {
				if (result is FaultEvent) 
					callback.fault(result);
				else
					callback.result(result);
			}
		}
	}
}