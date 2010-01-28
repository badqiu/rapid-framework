
package com.company.project.user_info.event
{
	import com.adobe.cairngorm.control.CairngormEvent;
	import appcommon.flex.page.PageRequest;

	public class ListUserInfoEvent extends CairngormEvent
	{
		public static var EVENT_NAME : String = "listUserInfoEvent";
		public var pageRequest:PageRequest;
		public function ListUserInfoEvent(pageRequest:PageRequest)
		{
			super(EVENT_NAME);
			this.pageRequest = pageRequest;
		}

	}
}