/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info.command
{

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	import com.company.project.user_info.*;
	import com.company.project.user_info.event.*;
	import com.company.project.vo.*;

	import appcommon.flex.base.*;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;

	import mx.rpc.IResponder;

	public class DeleteUserInfoCommand extends BaseResponder implements IResponder,ICommand
	{
		[Bindable]
		private var model : UserInfoModelLocator = UserInfoModelLocator.getInstance();
		private var ids : Array;

		public function execute(event:CairngormEvent):void
		{
			var ids : Array = DeleteUserInfoEvent(event).ids;
			var delegate: UserInfoDelegate = new UserInfoDelegate(this);
			this.ids = ids;
			delegate.del(ids);
		}

		override public function result(data:Object):void
		{
			CollectionUtils.removeByPropertyEqual(model.list,'userId',ids);
			if(model.list.length > 0) {
				model.selectedItem = model.list.getItemAt(0) as UserInfo;
			}else {
				model.selectedItem = new UserInfo();
			}
		}

	}
}