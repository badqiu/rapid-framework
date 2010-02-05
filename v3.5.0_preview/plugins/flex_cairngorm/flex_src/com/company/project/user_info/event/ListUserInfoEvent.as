/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info.event
{
	import appcommon.flex.event.CairngormCallbackEvent;
	import appcommon.flex.page.PageRequest;

	public class ListUserInfoEvent extends CairngormCallbackEvent
	{
		public static const EVENT_NAME : String = "listUserInfoEvent";
		public var pageRequest:PageRequest;
		public function ListUserInfoEvent(pageRequest:PageRequest)
		{
			super(EVENT_NAME);
			this.pageRequest = pageRequest;
		}

	}
}