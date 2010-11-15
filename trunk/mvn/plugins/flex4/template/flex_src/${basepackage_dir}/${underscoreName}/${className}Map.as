<#include "/actionscript_copyright.include">
<#assign className = table.className>   
<#assign classNameFirstLower = className?uncap_first>
<#assign underscoreName = table.underscoreName/>
<#assign classNameLowerCase = className?lower_case/>

package ${basepackage}.${underscoreName}
{
	import appcommon.flex.base.HashMap;

	[Bindable]
	public class ${className}Map extends HashMap
	{

		private static var instance:${className}Map;

		public function ${className}Map()
		{
			super();
			if (instance != null)
			{
				throw new Error("${className}Map is singleton");
			}
		}

		public static function getInstance():${className}Map
		{
			if (instance == null)
				instance=new ${className}Map();

			return instance;
		}
	}
}