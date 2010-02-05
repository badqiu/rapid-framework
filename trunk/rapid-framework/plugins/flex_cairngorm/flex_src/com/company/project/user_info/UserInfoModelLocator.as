/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info{
	import appcommon.flex.base.*;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;
	
	import com.adobe.cairngorm.model.IModelLocator;
	import com.company.project.model.*;
	
	import mx.collections.ArrayCollection;

	[Bindable]
	 public class UserInfoModelLocator implements IModelLocator
	 {

	    private static var instance : UserInfoModelLocator;

	    public function UserInfoModelLocator()
	    {
	       if ( instance != null )
	       {
	          throw new Error("UserInfoModelLocator is singleton" );
	       }
	    }

	    public static function getInstance() : UserInfoModelLocator
	    {
	       if ( instance == null )
	       		instance = new UserInfoModelLocator();

	       return instance;
	    }

		public var userInfo : UserInfo = null;
		//public var list : ArrayCollection = new ArrayCollection();
		
		public var page : Page = new Page();
	 }
}