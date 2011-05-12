<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>
package ${basepackage}.${underscoreName}.event
{
	import ${basepackage}.domain.${className};
	import flash.events.Event;

	public class Save${className}Event extends Event
	{
		public static const EVENT_NAME:String="save${className}Event";

		public var ${classNameFirstLower}:${className};

		public function Save${className}Event(${classNameFirstLower}:${className})
		{
			super(EVENT_NAME);
			this.${classNameFirstLower}=${classNameFirstLower};
		}
	}
}

