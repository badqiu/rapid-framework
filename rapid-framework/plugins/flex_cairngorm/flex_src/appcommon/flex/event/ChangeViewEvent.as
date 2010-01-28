package appcommon.flex.event
{
	import flash.events.Event;
	
	public class ChangeViewEvent extends Event
	{
		public var viewName : String;
		public function ChangeViewEvent(viewName : String)
		{
			super("changeViewEvent");
			this.viewName = viewName;
		}
	}
}