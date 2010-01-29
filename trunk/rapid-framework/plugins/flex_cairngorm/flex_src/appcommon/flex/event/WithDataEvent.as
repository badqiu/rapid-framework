package appcommon.flex.event
{
	import flash.events.Event;
	/**
	 * 通用event类，增加一个data属性用于传递数据
	 */
	public class WithDataEvent extends Event
	{
		public var data : *;
		public function WithDataEvent(type : String,data : *, bubbles : Boolean = false, cancelable : Boolean = false)
		{
			super(type,bubbles,cancelable);
			this.data = data;
		}
	}
}