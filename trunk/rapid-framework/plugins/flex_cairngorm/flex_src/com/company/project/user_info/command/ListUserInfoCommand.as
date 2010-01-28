/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info.command
{

	import com.adobe.cairngorm.control.CairngormEvent;
	import com.adobe.cairngorm.commands.ICommand;
	import com.company.project.user_info.*;
	import com.company.project.user_info.event.ListUserInfoEvent;
	import com.company.project.vo.*;

	import appcommon.flex.base.*;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;

	import mx.rpc.IResponder;

	public class ListUserInfoCommand extends BaseResponder implements  IResponder,ICommand
	{
		private var model : UserInfoModelLocator = UserInfoModelLocator.getInstance();

		public function execute(event:CairngormEvent):void
		{
			var pageRequest : PageRequest = ListUserInfoEvent(event).pageRequest;
			var delegate: UserInfoDelegate = new UserInfoDelegate(this);
			delegate.list(pageRequest);
		}

		override public function result(data:Object):void
		{
			model.list = data.result.result;
			if(model.list.length > 0){
				model.selectedItem = model.list.getItemAt(0) as UserInfo;
			}
			BeanUtils.copyProperties(model.page,data.result);
		}

	}
}