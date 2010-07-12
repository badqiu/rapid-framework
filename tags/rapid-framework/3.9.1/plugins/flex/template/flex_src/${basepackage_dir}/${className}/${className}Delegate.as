<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
 
package ${basepackage}.${className}
{
	import com.adobe.cairngorm.business.ServiceLocator;
	import mx.rpc.remoting.mxml.RemoteObject;
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	
	<#include "/actionscript_imports.include">
	
	public class ${className}Delegate extends BaseDelegate
	{
		private var ${classNameLower}FlexService: Object;
		public function ${className}Delegate(responder:IResponder)
		{
			this.responder = responder;
			//this.${classNameLower}FlexService = ServiceLocator.getInstance().getRemoteObject("${classNameLower}FlexService");
			this.${classNameLower}FlexService = new RemoteObject("${classNameLower}FlexService");
			this.${classNameLower}FlexService.endpoint = '../messagebroker/amf';
			this.${classNameLower}FlexService.showBusyCursor = true;
		}
		
		public function save(${classNameLower}:${className}):void{
			var call:AsyncToken = ${classNameLower}FlexService.save(${classNameLower});
			call.addResponder(this.responder);
		}
				
		public function del(ids:Array):void{
			var call:AsyncToken = ${classNameLower}FlexService.del(ids);
			call.addResponder(this.responder);
		}
		
		public function list(pageRequest:PageRequest):void{
			var call:AsyncToken = ${classNameLower}FlexService.list(pageRequest);
			call.addResponder(this.responder);
		}
		
	}
	
}