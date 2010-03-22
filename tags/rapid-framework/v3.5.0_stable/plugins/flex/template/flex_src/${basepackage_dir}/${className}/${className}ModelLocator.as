<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   

package ${basepackage}.${className}{
	import com.adobe.cairngorm.model.IModelLocator;
	
	import mx.collections.ArrayCollection;
	
	<#include "/actionscript_imports.include">
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
	  
		public var selectedItem: ${className} = new ${className}();
		public var list: ArrayCollection = new ArrayCollection(); 
		
		public var pageRequest: PageRequest = new PageRequest();
		public var page: Page = new Page(); 
		
	 }
}