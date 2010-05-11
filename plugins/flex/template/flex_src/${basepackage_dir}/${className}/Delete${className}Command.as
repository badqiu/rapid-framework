<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   

package ${basepackage}.${className}
{
	
	<#include "/actionscript_imports.include">
	
	import mx.rpc.IResponder;
	
	public class Delete${className}Command extends BaseResponder implements IResponder,ICommand
	{
		[Bindable]
		private var model : ${className}ModelLocator = ${className}ModelLocator.getInstance();
		private var ids : Array;
		public function Delete${className}Command(ids : Array)
		{
			this.ids = ids;
		}
	
		public function execute():void
		{
			var delegate: ${className}Delegate = new ${className}Delegate(this);
			delegate.del(ids);
		}
		
		override public function result(data:Object):void
		{
			CollectionUtils.removeByPropertyEqual(model.list,'${table.idColumn.columnNameLower}',ids);
			if(model.list.length > 0) {
				model.selectedItem = model.list.getItemAt(0) as ${className};
			}else {
				model.selectedItem = new ${className}();
			}
		}
		
	}
}