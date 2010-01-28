/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info
{
	import com.adobe.cairngorm.business.ServiceLocator;
	import mx.rpc.remoting.mxml.RemoteObject;
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;

	import appcommon.flex.base.*;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;

	import com.company.project.vo.*;


	public class UserInfoDelegate extends BaseDelegate
	{
		private var userInfoFlexService: Object;
		public function UserInfoDelegate(responder:IResponder)
		{
			this.responder = responder;
			this.userInfoFlexService = ServiceLocator.getInstance().getRemoteObject("userInfoFlexService");
			//this.userInfoFlexService = new RemoteObject("userInfoFlexService");
			//this.userInfoFlexService.endpoint = '../messagebroker/amf';
			//this.userInfoFlexService.showBusyCursor = true;
		}

		public function save(userInfo:UserInfo):void{
			var call:AsyncToken = userInfoFlexService.save(userInfo);
			call.addResponder(this.responder);
		}

		public function del(ids:Array):void{
			var call:AsyncToken = userInfoFlexService.del(ids);
			call.addResponder(this.responder);
		}

		public function list(pageRequest:PageRequest):void{
			var call:AsyncToken = userInfoFlexService.list(pageRequest);
			call.addResponder(this.responder);
		}

	}

}