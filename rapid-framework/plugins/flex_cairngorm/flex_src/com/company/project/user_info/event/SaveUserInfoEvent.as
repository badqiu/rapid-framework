
package com.company.project.user_info.event
{
	import com.adobe.cairngorm.control.CairngormEvent;
	import com.company.project.vo.UserInfo;
	
	import flash.display.DisplayObject;

	public class SaveUserInfoEvent extends CairngormEvent
	{
		public static var EVENT_NAME : String = "saveUserInfoEvent";
		public var userInfo:UserInfo;
		public var view : DisplayObject;
		public function SaveUserInfoEvent(userInfo:UserInfo,view : DisplayObject)
		{
			super(EVENT_NAME);
			this.userInfo = userInfo;
			this.view = view;
		}

	}
}