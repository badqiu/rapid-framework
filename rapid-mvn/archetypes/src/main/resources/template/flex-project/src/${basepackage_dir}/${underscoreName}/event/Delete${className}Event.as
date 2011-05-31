<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>
package ${basepackage}.${underscoreName}.event
{
	import flash.events.Event;


	public class Delete${className}Event extends Event
	{
		public static const EVENT_NAME:String="delete${className}Event";

		public var ids:Array;

		public function Delete${className}Event(ids:Array)
		{
			super(EVENT_NAME);
			this.ids=ids;
		}

	}
}

