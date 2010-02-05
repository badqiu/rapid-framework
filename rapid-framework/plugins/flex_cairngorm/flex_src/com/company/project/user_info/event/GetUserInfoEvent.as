/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info.event
{
	import appcommon.flex.event.CairngormCallbackEvent;
	import com.adobe.cairngorm.control.CairngormEvent;

	public class GetUserInfoEvent extends CairngormCallbackEvent
	{
		public static const EVENT_NAME : String = "getUserInfoEvent";
		public var id : Number;
		public function GetUserInfoEvent(id : Number)
		{
			super(EVENT_NAME);
			this.id = id;
		}

	}
}