<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.${underscoreName}.command
{
	import ${basepackage}.${underscoreName}.*;
	import ${basepackage}.${underscoreName}.event.Get${className}Event;
	import ${basepackage}.model.*;
	
	import appcommon.flex.base.*;
	import appcommon.flex.event.CairngormCallbackEvent;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;
	
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	import mx.controls.Alert;
	import mx.rpc.IResponder;

	public class Get${className}Command extends BaseResponder implements  IResponder,ICommand
	{
		private var model :${className}ModelLocator = ${className}ModelLocator.getInstance();
		
		public function execute(event:CairngormEvent):void
		{
			var id : Number = Get${className}Event(event).id;
			var delegate: ${className}Delegate = new ${className}Delegate([this,CairngormCallbackEvent(event).callback]);
			delegate.getById(id);
		}

		override public function result(event:Object):void
		{
			model.${classNameFirstLower} = event.result;
		}

	}
}