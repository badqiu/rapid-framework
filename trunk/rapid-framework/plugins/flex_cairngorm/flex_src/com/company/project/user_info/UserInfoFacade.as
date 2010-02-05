/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info
{
	import appcommon.flex.base.*;
	import appcommon.flex.event.Callback;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;
	
	import com.company.project.user_info.event.GetUserInfoEvent;
	import com.company.project.user_info.view.UserInfoDetailWindow;
	import com.company.project.vo.*;
	
	import flash.events.Event;
	
	import mx.controls.Alert;
	import mx.managers.PopUpManager;

	public class UserInfoFacade extends BaseFacade
	{
		public static function edit(id : Number) {
			var e : GetUserInfoEvent = new GetUserInfoEvent(id);
			e.callback = new Callback(onResult_edit,onFault_edit);
			e.dispatch();
		}
		
		public static function onResult_edit(e : Event) {
			var win : UserInfoDetailWindow = PopUpManager.createPopUp(null,UserInfoDetailWindow,true);
			win.userInfo = event.result;
			win.currentState = "edit";
		}
		
		public static function onFault_edit(e : Event) {
			Alert.show(String(e),"发生错误");
		}
		
		public static function del(id) {
			
		}
		
		
	}

}