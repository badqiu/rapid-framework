<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.${underscoreName}.event
{
	import appcommon.flex.event.CairngormCallbackEvent;
	import appcommon.flex.page.PageRequest;

	public class List${className}Event extends CairngormCallbackEvent
	{
		public static const EVENT_NAME : String = "list${className}Event";
		public var pageRequest:PageRequest;
		public function List${className}Event(pageRequest:PageRequest)
		{
			super(EVENT_NAME);
			this.pageRequest = pageRequest;
		}

	}
}