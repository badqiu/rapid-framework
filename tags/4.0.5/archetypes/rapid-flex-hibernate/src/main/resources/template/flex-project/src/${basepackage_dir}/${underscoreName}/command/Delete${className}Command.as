<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>
package ${basepackage}.${underscoreName}.command
{
	<#include "/actionscript_imports.include"/>	

	import mx.controls.Alert;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class Delete${className}Command
	{
		[Inject(id="service")]
		public var ${classNameFirstLower}Facade:RemoteObject;

		[Inject(id="${classNameFirstLower}Model")]
		public var ${classNameFirstLower}Model:${className}Model;

		public var ids:Array;


		public function execute(event:Delete${className}Event):AsyncToken
		{
			ids=event.ids;
			${classNameFirstLower}Facade.destination="${classNameFirstLower}Facade";
			return ${classNameFirstLower}Facade.del(ids);
		}

		public function result(event:ResultEvent)
		{
			CollectionUtils.removeByPropertyEqual(${classNameFirstLower}Model.page.result, '${classNameFirstLower}Id', ids);
		}

		public function error(event:FaultEvent)
		{
			Alert.show(event.fault.toString());
		}

	}
}

