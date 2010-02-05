/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info.event
{
	import appcommon.flex.event.CairngormCallbackEvent;
	import com.adobe.cairngorm.control.CairngormEvent;

	public class DeleteUserInfoEvent extends CairngormCallbackEvent
	{
		public static const EVENT_NAME : String = "deleteUserInfoEvent";
		[ArrayElementType("Number")]public var ids:Array;
		public function DeleteUserInfoEvent(ids:Array)
		{
			super(EVENT_NAME);
			this.ids = ids;
		}

	}
}