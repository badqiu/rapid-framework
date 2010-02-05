<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.${underscoreName}
{
	import com.adobe.cairngorm.business.ServiceLocator;
	import mx.rpc.remoting.mxml.RemoteObject;
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;

	import appcommon.flex.base.*;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;

	import ${basepackage}.model.*;


	public class ${className}Delegate extends BaseDelegate
	{
		private var ${classNameFirstLower}FlexService: Object;
		public function ${className}Delegate(responders:Array)
		{
			this.responders = responders;
			this.${classNameFirstLower}FlexService = ServiceLocator.getInstance().getRemoteObject("${classNameFirstLower}FlexService");
			//this.${classNameFirstLower}FlexService = new RemoteObject("${classNameFirstLower}FlexService");
			//this.${classNameFirstLower}FlexService.endpoint = '../messagebroker/amf';
			//this.${classNameFirstLower}FlexService.showBusyCursor = true;
		}

		public function save(${classNameFirstLower}:${className}):void{
			var call:AsyncToken = ${classNameFirstLower}FlexService.save(${classNameFirstLower});
			addResponders(call);
		}

		public function del(ids:Array):void{
			var call:AsyncToken = ${classNameFirstLower}FlexService.del(ids);
			addResponders(call);
		}

		public function list(pageRequest:PageRequest):void{
			var call:AsyncToken = ${classNameFirstLower}FlexService.list(pageRequest);
			addResponders(call);
		}

		public function getById(id : Number):void{
			var call:AsyncToken = ${classNameFirstLower}FlexService.getById(id)
			addResponders(call);
		}
	}

}