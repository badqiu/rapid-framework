<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.${underscoreName}.event
{
	import appcommon.flex.event.CairngormCallbackEvent;
	import com.adobe.cairngorm.control.CairngormEvent;

	public class Delete${className}Event extends CairngormCallbackEvent
	{
		public static const EVENT_NAME : String = "delete${className}Event";
		[ArrayElementType("Number")]public var ids:Array;
		public function Delete${className}Event(ids:Array)
		{
			super(EVENT_NAME);
			this.ids = ids;
		}

	}
}