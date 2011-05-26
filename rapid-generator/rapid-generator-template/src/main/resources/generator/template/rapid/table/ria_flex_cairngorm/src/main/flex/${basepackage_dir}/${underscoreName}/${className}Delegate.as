<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.${underscoreName}
{
	import com.adobe.cairngorm.business.ServiceLocator;
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;

	import appcommon.flex.base.*;
	import appcommon.flex.page.*;
	import appcommon.flex.util.*;

	import ${basepackage}.model.*;


	public class ${className}Delegate extends BaseDelegate
	{
		private var ${classNameFirstLower}Service: Object;
		public function ${className}Delegate(responders:Array)
		{
			this.responders = responders;
			this.${classNameFirstLower}Service = (ServiceLocator.getInstance())[("${classNameFirstLower}Service")];
		}

		public function save(${classNameFirstLower}:${className}):void{
			var call:AsyncToken = ${classNameFirstLower}Service.save(${classNameFirstLower});
			addResponders(call);
		}

		public function del(ids:Array):void{
			var call:AsyncToken = ${classNameFirstLower}Service.del(ids);
			addResponders(call);
		}

		public function list(pageRequest:PageRequest):void{
			var call:AsyncToken = ${classNameFirstLower}Service.list(pageRequest);
			addResponders(call);
		}

		public function getById(id : Number):void{
			var call:AsyncToken = ${classNameFirstLower}Service.getById(id)
			addResponders(call);
		}
	}

}