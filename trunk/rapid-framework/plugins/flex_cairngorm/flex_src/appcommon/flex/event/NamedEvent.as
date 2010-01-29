package appcommon.flex.event
{
	import flash.events.Event;
	/**
	 * 通用event类，增加一个name属性用于传递数据
	 */	
	public class NamedEvent extends Event
	{
		public var name : String;
		public function NamedEvent(type : String,name : String, bubbles : Boolean = false, cancelable : Boolean = false)
		{
			super("changeViewEvent");
			this.name = name;
		}
	}
}