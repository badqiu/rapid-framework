/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info.command
{

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	import com.company.project.user_info.*;
	import com.company.project.user_info.event.SaveUserInfoEvent;
	import com.company.project.vo.*;
	
	import flash.display.DisplayObject;
	
	import appcommon.flex.base.*;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;
	
	import mx.controls.Alert;
	import mx.core.IFlexDisplayObject;
	import mx.managers.PopUpManager;
	import mx.rpc.IResponder;

	public class SaveUserInfoCommand extends BaseResponder implements  IResponder,ICommand
	{
		private var model :UserInfoModelLocator = UserInfoModelLocator.getInstance();
		
		private var currentView : IFlexDisplayObject;
		public function execute(event:CairngormEvent):void
		{
			this.currentView = IFlexDisplayObject(SaveUserInfoEvent(event).view);
			var userInfo : UserInfo = SaveUserInfoEvent(event).userInfo;
			var delegate: UserInfoDelegate = new UserInfoDelegate(this);
			delegate.save(userInfo);
		}

		override public function result(data:Object):void
		{
			var userInfo:UserInfo = data.result as UserInfo;
			if(CollectionUtils.isPropertyEqual(model.list,userInfo,"userId")) {
				// is update
				BeanUtils.copyProperties(model.selectedItem,data.result);
				model.list.refresh();
			}else {
				// is new
				model.list.addItemAt(userInfo, 0);
				model.selectedItem = userInfo;
			}
			PopUpManager.removePopUp(currentView);
		}

	}
}