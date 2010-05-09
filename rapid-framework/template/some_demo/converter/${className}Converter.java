<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.converter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.company.project.facade.dto.UserInfoDTO;
import com.company.project.model.UserInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

<#include "/java_imports.include">

public class ${className}Converter {

	<@generateConvertMethod "","VO"/>
	<@generateConvertMethod "VO",""/>
	
	<@generateConvertMethod "Form",""/>
	<@generateConvertMethod "","Form"/>
}
        
<#macro generateConvertMethod sourceSuffix targetSuffix>
	public ${className}${targetSuffix} convert(${className}${sourceSuffix} source) {
		${className}${targetSuffix} target = new ${className}${targetSuffix}();
	
		<#list table.notPkColumns as column>
		target.set${column.columnName}(source.get${column.columnName}());
		</#list>
		
		return target;
	}

	public static List<${className}${targetSuffix}> convertFrom(List<${className}${sourceSuffix}> list) {
		List<${className}${targetSuffix}> results = new ArrayList();
		for(UserInfo source : list) {
			results.add(convert(source));
		}
		return results;
	}

	
</#macro>