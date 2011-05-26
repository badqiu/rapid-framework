<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>
package ${basepackage}.${underscoreName}.command
{

	<#include "/actionscript_imports.include"/>		
	import ${basepackage}.${underscoreName}.view.${className}DetailWindow;

	import mx.controls.Alert;
	import mx.managers.PopUpManager;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class Save${className}Command
	{
		[Inject(id="service")]
		public var ${classNameFirstLower}Facade:RemoteObject;

		[Inject(id="${classNameFirstLower}Model")]
		public var ${classNameFirstLower}Model:${className}Model;

		public var ${classNameFirstLower}DetailWindow:${className}DetailWindow;

		public function execute(event:Save${className}Event):AsyncToken
		{
			${classNameFirstLower}DetailWindow=event.target as ${className}DetailWindow;
			${classNameFirstLower}Facade.destination="${classNameFirstLower}Facade";
			return ${classNameFirstLower}Facade.save(event.${classNameFirstLower});
		}

		public function result(event:ResultEvent)
		{
			PopUpManager.removePopUp(${classNameFirstLower}DetailWindow);
			var ${classNameFirstLower}:${className}=event.result as ${className};
			if (CollectionUtils.updateListItemIfPropertyEqual(${classNameFirstLower}Model.page.result, ${classNameFirstLower}, "${classNameFirstLower}Id"))
			{
				// is update
			}
			else
			{
				// is new
				${classNameFirstLower}Model.page.result.addItemAt(${classNameFirstLower}, 0);
			}
		}

		public function error(event:FaultEvent)
		{
			Alert.show(event.fault.toString());
		}

	}
}

