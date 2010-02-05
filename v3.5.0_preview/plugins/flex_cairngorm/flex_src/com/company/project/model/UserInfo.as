/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.model{
	
	import appcommon.flex.base.*;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;
	
	import com.company.project.model.*;

	
	[Bindable]
	[RemoteClass(alias="com.company.project.model.UserInfo")]
	[ResourceBundle("UserInfo")]
	public class UserInfo extends BaseEntity
	{
		
		//alias
		public static const ALIAS_USER_ID:String = "userId";
		public static const ALIAS_USERNAME:String = "username";
		public static const ALIAS_PASSWORD:String = "password";
		public static const ALIAS_BIRTH_DATE:String = "birthDate";
		public static const ALIAS_SEX:String = "sex";
		public static const ALIAS_AGE:String = "age";

		//date formats
		public static const FORMAT_BIRTH_DATE : String = DATE_FORMAT;
				
		//columns START
		public var userId:Number;
		public var username:String;
		public var password:String;
		public var birthDate:Date;
		public var sex:Number;
		public var age:Number;
		//columns END

		[Transient]
		public function get birthDateString():String{
			return DateConvertUtils.format(this.birthDate,FORMAT_BIRTH_DATE);
		}		
		
		
	}
	
	
}