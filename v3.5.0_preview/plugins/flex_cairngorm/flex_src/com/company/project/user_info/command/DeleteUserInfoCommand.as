/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info.command
{
	import com.company.project.user_info.*;
	import com.company.project.user_info.event.*;
	import com.company.project.model.*;
	
	import appcommon.flex.base.*;
	import appcommon.flex.event.CairngormCallbackEvent;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;	
	import mx.rpc.IResponder;

	public class DeleteUserInfoCommand extends BaseResponder implements IResponder,ICommand
	{
		[Bindable]
		private var model : UserInfoModelLocator = UserInfoModelLocator.getInstance();
		private var ids : Array;

		public function execute(event:CairngormEvent):void
		{
			var ids : Array = DeleteUserInfoEvent(event).ids;
			var delegate: UserInfoDelegate = new UserInfoDelegate([this,CairngormCallbackEvent(event).callback]);
			this.ids = ids;
			delegate.del(ids);
		}

		override public function result(event:Object):void
		{
			CollectionUtils.removeByPropertyEqual(model.page.result,'userId',ids);
		}

	}
}