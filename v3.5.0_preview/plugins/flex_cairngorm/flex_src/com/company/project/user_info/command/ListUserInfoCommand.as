/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info.command
{
	
	import com.company.project.user_info.*;
	import com.company.project.model.*;
	import com.company.project.user_info.event.ListUserInfoEvent;
	
	import appcommon.flex.base.*;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;
	import appcommon.flex.event.CairngormCallbackEvent;

	import com.adobe.cairngorm.control.CairngormEvent;
	import com.adobe.cairngorm.commands.ICommand;
	import mx.rpc.IResponder;
	
	public class ListUserInfoCommand extends BaseResponder implements  IResponder,ICommand
	{
		private var model : UserInfoModelLocator = UserInfoModelLocator.getInstance();

		public function execute(event:CairngormEvent):void
		{
			var pageRequest : PageRequest = ListUserInfoEvent(event).pageRequest;
			var delegate: UserInfoDelegate = new UserInfoDelegate([this,CairngormCallbackEvent(event).callback]);
			delegate.list(pageRequest);
		}

		override public function result(event:Object):void
		{
			model.page = event.result;
			//BeanUtils.copyProperties(model.page,event.result);
		}

	}
}