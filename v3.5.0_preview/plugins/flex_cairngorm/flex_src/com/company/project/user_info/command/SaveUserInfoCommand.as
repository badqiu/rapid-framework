/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info.command
{

	import com.company.project.user_info.*;
	import com.company.project.user_info.event.SaveUserInfoEvent;
	import com.company.project.model.*;
	
	import appcommon.flex.base.*;
	import appcommon.flex.event.CairngormCallbackEvent;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;
	
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IViewCursor;
	import mx.rpc.IResponder;

	public class SaveUserInfoCommand extends BaseResponder implements  IResponder,ICommand
	{
		private var model :UserInfoModelLocator = UserInfoModelLocator.getInstance();
		
		private var callbackEvent : CairngormCallbackEvent;
		public function execute(event:CairngormEvent):void
		{
			this.callbackEvent = CairngormCallbackEvent(event);
			
			var userInfo : UserInfo = SaveUserInfoEvent(event).userInfo;
			var delegate: UserInfoDelegate = new UserInfoDelegate([this,CairngormCallbackEvent(event).callback]);
			delegate.save(userInfo);
		}

		override public function result(event:Object):void
		{
			var userInfo:UserInfo = event.result as UserInfo;
			if(CollectionUtils.updateListItemIfPropertyEqual(model.page.result,userInfo,"userId")) {
				// is update
			}else {
				// is new
				model.page.result.addItemAt(userInfo, 0);
			}
		}

	}
}