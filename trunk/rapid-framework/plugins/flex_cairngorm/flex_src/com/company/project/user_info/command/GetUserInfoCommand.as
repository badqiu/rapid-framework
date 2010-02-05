/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info.command
{
	import com.company.project.user_info.*;
	import com.company.project.user_info.event.GetUserInfoEvent;
	import com.company.project.model.*;
	
	import appcommon.flex.base.*;
	import appcommon.flex.event.CairngormCallbackEvent;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;
	
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	import mx.controls.Alert;
	import mx.rpc.IResponder;

	public class GetUserInfoCommand extends BaseResponder implements  IResponder,ICommand
	{
		private var model :UserInfoModelLocator = UserInfoModelLocator.getInstance();
		
		public function execute(event:CairngormEvent):void
		{
			var id : Number = GetUserInfoEvent(event).id;
			var delegate: UserInfoDelegate = new UserInfoDelegate([this,CairngormCallbackEvent(event).callback]);
			delegate.getById(id);
		}

		override public function result(event:Object):void
		{
			model.userInfo = event.result;
		}

	}
}