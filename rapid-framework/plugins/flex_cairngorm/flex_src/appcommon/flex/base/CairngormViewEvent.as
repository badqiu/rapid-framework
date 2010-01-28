package appcommon.flex.base
{
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import mx.core.UIComponent;
	
	public class CairngormViewEvent extends CairngormEvent
	{
	  /**
	  * 产生事件的view 
	  */
	  public view : UIComponent
	  
      /**
       * Constructor, takes the event name (type) and data object (defaults to null)
       * and also defaults the standard Flex event properties bubbles and cancelable
       * to true and false respectively.
       */
      public function CairngormViewEvent( type : String,bubbles : Boolean = false, cancelable : Boolean = false,view : UIComponent = null )
      {
         super( type, bubbles, cancelable );
         this.view = view;
      }
	}
}