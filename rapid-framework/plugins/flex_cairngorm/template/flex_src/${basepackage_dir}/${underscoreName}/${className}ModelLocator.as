<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.${underscoreName}{
	import appcommon.flex.base.*;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;
	
	import com.adobe.cairngorm.model.IModelLocator;
	import ${basepackage}.model.*;
	
	import mx.collections.ArrayCollection;

	[Bindable]
	 public class ${className}ModelLocator implements IModelLocator
	 {

	    private static var instance : ${className}ModelLocator;

	    public function ${className}ModelLocator()
	    {
	       if ( instance != null )
	       {
	          throw new Error("${className}ModelLocator is singleton" );
	       }
	    }

	    public static function getInstance() : ${className}ModelLocator
	    {
	       if ( instance == null )
	       		instance = new ${className}ModelLocator();

	       return instance;
	    }

		public var ${classNameFirstLower} : ${className} = null;
		//public var list : ArrayCollection = new ArrayCollection();
		
		public var page : Page = new Page();
	 }
}