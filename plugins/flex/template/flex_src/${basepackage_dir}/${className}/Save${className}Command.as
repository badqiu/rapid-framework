<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   

package ${basepackage}.${className}
{
	
	import mx.collections.ArrayCollection;
	import mx.rpc.IResponder;
	
	<#include "/actionscript_imports.include">
	
	public class Save${className}Command extends BaseResponder implements  IResponder,ICommand
	{
		private var model :${className}ModelLocator = ${className}ModelLocator.getInstance();
		
		private var ${classNameLower} : ${className};
		
		public function Save${className}Command(${classNameLower} : ${className})
		{
			this.${classNameLower} = ${classNameLower};
		}
	
		public function execute():void
		{
			var delegate: ${className}Delegate = new ${className}Delegate(this);
			delegate.save(${classNameLower});
		}
		
		override public function result(data:Object):void
		{
			var ${classNameLower}:${className} = data.result as ${className};
			if(CollectionUtils.isPropertyEqual(model.list,${classNameLower},"${table.idColumn.columnNameLower}")) {
				// is update
				BeanUtils.copyProperties(model.selectedItem,data.result);
				model.list.refresh();
			}else {
				// is new 
				model.list.addItemAt(${classNameLower}, 0);
				model.selectedItem = ${classNameLower};
			}
		}
		
	}
}