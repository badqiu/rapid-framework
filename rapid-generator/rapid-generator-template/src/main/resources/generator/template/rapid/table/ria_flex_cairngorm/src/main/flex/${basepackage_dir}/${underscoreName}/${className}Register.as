<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>


package ${basepackage}.${underscoreName}
{
	import ${basepackage}.${underscoreName}.event.*;
	import ${basepackage}.${underscoreName}.command.*;
	import ${basepackage}.${underscoreName}.*;
	
	import com.adobe.cairngorm.control.FrontController;
	
	public class ${className}Register
	{
		public static function initialiseCommands(frontController:FrontController) : void
		{
			frontController.addCommand(Delete${className}Event.EVENT_NAME, Delete${className}Command);
			frontController.addCommand(List${className}Event.EVENT_NAME, List${className}Command);
			frontController.addCommand(Save${className}Event.EVENT_NAME, Save${className}Command);
			frontController.addCommand(Get${className}Event.EVENT_NAME, Get${className}Command);
		}	
	}
	
}
