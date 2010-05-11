<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.${underscoreName}.command
{
	
	import ${basepackage}.${underscoreName}.*;
	import ${basepackage}.model.*;
	import ${basepackage}.${underscoreName}.event.List${className}Event;
	
	import appcommon.flex.base.*;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;
	import appcommon.flex.event.CairngormCallbackEvent;

	import com.adobe.cairngorm.control.CairngormEvent;
	import com.adobe.cairngorm.commands.ICommand;
	import mx.rpc.IResponder;
	
	public class List${className}Command extends BaseResponder implements  IResponder,ICommand
	{
		private var model : ${className}ModelLocator = ${className}ModelLocator.getInstance();

		public function execute(event:CairngormEvent):void
		{
			var pageRequest : PageRequest = List${className}Event(event).pageRequest;
			var delegate: ${className}Delegate = new ${className}Delegate([this,CairngormCallbackEvent(event).callback]);
			delegate.list(pageRequest);
		}

		override public function result(event:Object):void
		{
			model.page = event.result;
			//BeanUtils.copyProperties(model.page,event.result);
		}

	}
}