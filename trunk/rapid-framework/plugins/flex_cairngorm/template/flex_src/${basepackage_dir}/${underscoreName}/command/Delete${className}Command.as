<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.${underscoreName}.command
{
	import ${basepackage}.${underscoreName}.*;
	import ${basepackage}.${underscoreName}.event.*;
	import ${basepackage}.model.*;
	
	import appcommon.flex.base.*;
	import appcommon.flex.event.CairngormCallbackEvent;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;

	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;	
	import mx.rpc.IResponder;

	public class Delete${className}Command extends BaseResponder implements IResponder,ICommand
	{
		[Bindable]
		private var model : ${className}ModelLocator = ${className}ModelLocator.getInstance();
		private var ids : Array;

		public function execute(event:CairngormEvent):void
		{
			var ids : Array = Delete${className}Event(event).ids;
			var delegate: ${className}Delegate = new ${className}Delegate([this,CairngormCallbackEvent(event).callback]);
			this.ids = ids;
			delegate.del(ids);
		}

		override public function result(event:Object):void
		{
			CollectionUtils.removeByPropertyEqual(model.page.result,'${table.idColumn.columnNameFirstLower}',ids);
		}

	}
}