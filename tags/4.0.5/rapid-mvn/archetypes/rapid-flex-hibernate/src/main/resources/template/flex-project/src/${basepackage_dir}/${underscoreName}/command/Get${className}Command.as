<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>
package ${basepackage}.${underscoreName}.command
{

	<#include "/actionscript_imports.include"/>	

	import mx.controls.Alert;
	import mx.managers.PopUpManager;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class Get${className}Command
	{
		[Inject(id="service")]
		public var ${classNameFirstLower}Facade:RemoteObject;

		[Inject(id="${classNameFirstLower}Model")]
		public var ${classNameFirstLower}Model:${className}Model;

		public function execute(event:Get${className}Event):AsyncToken
		{
			${classNameFirstLower}Facade.destination="${classNameFirstLower}Facade";
			return ${classNameFirstLower}Facade.getById(event.id);
		}

		public function result(event:ResultEvent)
		{
			${classNameFirstLower}Model.${classNameFirstLower}=event.result as ${className};
		}

		public function error(event:FaultEvent)
		{
			Alert.show(event.fault.toString());
		}

	}
}

