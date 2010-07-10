<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.repository.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import ${basepackage}.model.enums.*;

/**
 * ${table.tableAlias}
 * @author zhongxuan
 */
public class ${className}  implements java.io.Serializable{
    private static final long serialVersionUID = 3148176768559230877L;
    
    <#list table.columns as column>
    <#if column.enumColumn>
    private ${column.enumClassName} ${column.columnNameLower};
    <#else>
    private ${column.javaType} ${column.columnNameLower};
    </#if>
    </#list>

<@generateConstructor className/>
<@generateJavaColumns/>
<@generateJavaOneToMany/>
<@generateJavaManyToOne/>

    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        <#list table.pkColumns as column>
            .append(get${column.columnName}())
        </#list>
            .toHashCode();
    }
    
    public boolean equals(Object obj) {
        if(obj instanceof ${className} == false) return false;
        if(this == obj) return true;
        ${className} other = (${className})obj;
        return new EqualsBuilder()
            <#list table.pkColumns as column>
            .append(get${column.columnName}(),other.get${column.columnName}())
            </#list>
            .isEquals();
    }
}

<#macro generateJavaColumns>
    <#list table.columns as column> 
    <#if column.enumColumn>
    public void set${column.columnName}(${column.enumClassName} value) {
        this.${column.columnNameLower} = value;
    }
    
    public ${column.enumClassName} get${column.columnName}() {
        return this.${column.columnNameLower};
    }   
    <#else>
    public void set${column.columnName}(${column.javaType} value) {
        this.${column.columnNameLower} = value;
    }
    
    public ${column.javaType} get${column.columnName}() {
        return this.${column.columnNameLower};
    }
    </#if>
    </#list>
</#macro>

<#macro generateJavaOneToMany>
    <#list table.exportedKeys.associatedTables?values as foreignKey>
    <#assign fkSqlTable = foreignKey.sqlTable>
    <#assign fkTable    = fkSqlTable.className>
    <#assign fkPojoClass = fkSqlTable.className>
    <#assign fkPojoClassVar = fkPojoClass?uncap_first>
    
    private Set ${fkPojoClassVar}s = new HashSet(0);
    public void set${fkPojoClass}s(Set<${fkPojoClass}> ${fkPojoClassVar}){
        this.${fkPojoClassVar}s = ${fkPojoClassVar};
    }
    
    public Set<${fkPojoClass}> get${fkPojoClass}s() {
        return ${fkPojoClassVar}s;
    }
    </#list>
</#macro>

<#macro generateJavaManyToOne>
    <#list table.importedKeys.associatedTables?values as foreignKey>
    <#assign fkSqlTable = foreignKey.sqlTable>
    <#assign fkTable    = fkSqlTable.className>
    <#assign fkPojoClass = fkSqlTable.className>
    <#assign fkPojoClassVar = fkPojoClass?uncap_first>
    
    private ${fkPojoClass} ${fkPojoClassVar};
    
    public void set${fkPojoClass}(${fkPojoClass} ${fkPojoClassVar}){
        this.${fkPojoClassVar} = ${fkPojoClassVar};
    }
    
    public ${fkPojoClass} get${fkPojoClass}() {
        return ${fkPojoClassVar};
    }
    </#list>
</#macro>

