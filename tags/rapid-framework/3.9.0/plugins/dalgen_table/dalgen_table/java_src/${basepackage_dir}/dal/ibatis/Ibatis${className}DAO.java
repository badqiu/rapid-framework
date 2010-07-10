<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dal.ibatis;

import org.springframework.stereotype.Component;


public class Ibatis${className}DAO extends BaseIbatis${className}DAO{

}
