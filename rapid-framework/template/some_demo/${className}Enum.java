<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package cn.org.rapid_framework.util.fortest_enum;
import cn.org.rapid_framework.util.KeyValue;
import cn.org.rapid_framework.util.KeyValueUtils;
import cn.org.rapid_framework.util.fortest_enum.SomeTypeEnum;

public class ${className}Enum {

<#list table.columns as column>
	<#if column.enumColumn>
		<@genEnumClassBody column/>
	</#if>
</#list>

}

<#macro genEnumClassBody column>
	public static enum ${column.enumClassName} implements KeyValue<String,String>{
		<#list column.enumMap?keys as key>
		${key}(${key},${column.enumMap[key]})<#if key_has_next>,</#if>
		</#list>
		;
		
		private final String key;
		private final String value;
		${column.enumClassName}(String key,String value) {
			this.key = key;
			this.value = value;
		}
		
		public static ${column.enumClassName} getByKey(String key) {
			return KeyValueUtils.getByKey(key, values());
		}
		
		public static ${column.enumClassName} getByValue(String value) {
			return KeyValueUtils.getByValue(value, values());
		}
		
		public static ${column.enumClassName} getRequiredByKey(String key) {
			return KeyValueUtils.getRequiredByKey(key, values());
		}
		
		public String getKey() {
			return key;
		}
	
		public String getValue() {
			return value;
		}
		
	}
</#macro>