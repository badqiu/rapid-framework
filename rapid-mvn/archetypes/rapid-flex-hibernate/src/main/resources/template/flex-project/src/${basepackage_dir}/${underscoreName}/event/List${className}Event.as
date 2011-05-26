<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>
package ${basepackage}.${underscoreName}.event
{
	import ${basepackage}.${underscoreName}.query.${className}Query;
	import flash.events.Event;

	public class List${className}Event extends Event
	{
		public static const EVENT_NAME:String="list${className}Event";

		public var query:${className}Query;

		public function List${className}Event(query:${className}Query)
		{
			super(EVENT_NAME);
			this.query=query;
		}
	}
}

