package appcommon.flex.base
{
	import mx.rpc.AsyncToken;
	import mx.rpc.Responder;
	
	public class BaseDelegate
	{
		protected var responders : Array;
		
		public function BaseDelegate()
		{
		}
		
		public function addResponders(token : AsyncToken) : void {
			for each (var r in responders) {
				if(r != null)
					token.addResponder(r);
			}
		}
	}
}