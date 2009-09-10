<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   

package ${basepackage}.${className}
{
	
	import mx.rpc.IResponder;
	
	<#include "/actionscript_imports.include">
	
	public class List${className}Command extends BaseResponder implements  IResponder,ICommand
	{
		private var pageRequest : PageRequest;
		private var model : ${className}ModelLocator = ${className}ModelLocator.getInstance();
		public function List${className}Command(pageRequest : PageRequest)
		{
			this.pageRequest = pageRequest;
		}
	
		public function execute():void
		{
			var delegate: ${className}Delegate = new ${className}Delegate(this);
			delegate.list(pageRequest);
		}
		
		override public function result(data:Object):void
		{
			model.list = data.result.result;
			if(model.list.length > 0){
				model.selectedItem = model.list.getItemAt(0) as ${className};
			}
			BeanUtils.copyProperties(model.page,data.result);
		}
		
	}
}