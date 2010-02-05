/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info.event
{
	import com.company.project.model.UserInfo;
	
	import appcommon.flex.event.CairngormCallbackEvent;
	import flash.display.DisplayObject;

	public class SaveUserInfoEvent extends CairngormCallbackEvent
	{
		public static const EVENT_NAME : String = "saveUserInfoEvent";
		public var userInfo:UserInfo;
		public function SaveUserInfoEvent(userInfo:UserInfo)
		{
			super(EVENT_NAME);
			this.userInfo = userInfo;
		}

	}
}