package appcommon.flex.event
{
	import com.adobe.cairngorm.control.CairngormEvent;
	import mx.rpc.IResponder;
	
	/**
	 * 为CairngormEvent增加一个callback属性,用于回调
	 */
	public class CairngormCallbackEvent extends CairngormEvent
	{
	  /**
	  * callback,用于回调
	  */
	  public var callback : IResponder;
	  
      /**
       * Constructor
       */
      public function CairngormCallbackEvent( type : String,callback : IResponder = null,bubbles : Boolean = false, cancelable : Boolean = false)
      {
         super( type, bubbles, cancelable );
         this.callback = callback;
      }
      
	}
}