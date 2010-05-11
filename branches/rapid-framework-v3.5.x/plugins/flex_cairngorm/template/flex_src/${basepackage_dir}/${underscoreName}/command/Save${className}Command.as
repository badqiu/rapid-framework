<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.${underscoreName}.command
{

	import ${basepackage}.${underscoreName}.*;
	import ${basepackage}.${underscoreName}.event.Save${className}Event;
	import ${basepackage}.model.*;
	
	import appcommon.flex.base.*;
	import appcommon.flex.event.CairngormCallbackEvent;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;
	
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IViewCursor;
	import mx.rpc.IResponder;

	public class Save${className}Command extends BaseResponder implements  IResponder,ICommand
	{
		private var model :${className}ModelLocator = ${className}ModelLocator.getInstance();
		
		public function execute(event:CairngormEvent):void
		{
			var ${classNameFirstLower} : ${className} = Save${className}Event(event).${classNameFirstLower};
			var delegate: ${className}Delegate = new ${className}Delegate([this,CairngormCallbackEvent(event).callback]);
			delegate.save(${classNameFirstLower});
		}

		override public function result(event:Object):void
		{
			var ${classNameFirstLower}:${className} = event.result as ${className};
			if(CollectionUtils.updateListItemIfPropertyEqual(model.page.result,${classNameFirstLower},"${table.idColumn.columnNameFirstLower}")) {
				// is update
			}else {
				// is new
				model.page.result.addItemAt(${classNameFirstLower}, 0);
			}
		}

	}
}