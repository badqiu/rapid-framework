package appcommon.flex.event
{
	import mx.rpc.IResponder;
	
	public class Callback implements IResponder
	{
		/** Result handler function */
		public var resultHandler   : Function;
    	/** Fault handler function */
		public var faultHandler    : Function;
		
		public function Callback(resultFunc  : Function,
              					 faultFunc   : Function = null)
		{
			this.resultHandler = resultFunc;
			this.faultHandler = faultFunc;
		}
		
		/** Required method to support the IResponder interface */
		public function result(info:Object):void       
		{    
			if (resultHandler != null) 
				resultHandler(info);			
		}
		/** Required method to support the IResponder interface */
		public function fault(info:Object):void
		{
				if (faultHandler != null) 
					faultHandler(info);			
		}
	}
	
}