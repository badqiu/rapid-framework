/*
 * Copyright 2008 [rapid-framework], Inc. All rights reserved.
 * Website: http://www.rapid-framework.org.cn
 */


package com.company.project.user_info{
	import com.adobe.cairngorm.model.IModelLocator;

	import mx.collections.ArrayCollection;

	import appcommon.flex.base.*;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;

	import com.company.project.vo.*;

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

		public var selectedItem: UserInfo = new UserInfo();
		public var list: ArrayCollection = new ArrayCollection();

		public var page: Page = new Page();

	 }
}