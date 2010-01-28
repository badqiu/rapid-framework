package appcommon.flex.event
{
	import flash.events.Event;
	
	public class EditObjectEvent extends Event
	{
		public var data : *;
		public function EditObjectEvent(data : *)
		{
			super("editObjectEvent");
			this.data = data;
		}

	}
}