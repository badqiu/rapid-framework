package appcommon.flex.event
{
	import flash.events.Event;
	
	public class SelectObjectEvent extends Event
	{
		public var data : *;
		public function SelectObjectEvent(data : *)
		{
			super("selectObjectEvent");
			this.data = data;
		}

	}
}