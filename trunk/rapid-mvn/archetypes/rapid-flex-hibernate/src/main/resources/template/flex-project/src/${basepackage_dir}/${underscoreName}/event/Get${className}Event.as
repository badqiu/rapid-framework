<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>
package ${basepackage}.${underscoreName}.event
{

	import flash.events.Event;

	public class Get${className}Event extends Event
	{
		public static const EVENT_NAME:String="get${className}Event";

		public var id:Number;

		public function Get${className}Event(id:Number)
		{
			super(EVENT_NAME);
			this.id=id;
		}
	}
}

