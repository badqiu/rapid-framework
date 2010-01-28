
package com.company.project.user_info.event
{
	import com.adobe.cairngorm.control.CairngormEvent;

	public class DeleteUserInfoEvent extends CairngormEvent
	{
		public static var EVENT_NAME : String = "deleteUserInfoEvent";
		[ArrayElementType("Number")]public var ids:Array;
		public function DeleteUserInfoEvent(ids:Array)
		{
			super(EVENT_NAME);
			this.ids = ids;
		}

	}
}