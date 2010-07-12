<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import junit.framework.TestCase;

<#include "/java_imports.include">
public class ${className}ServiceTest extends TestCase{
    ${className}Service ${classNameLower}Service;
    
    public void test_find_some_thing() {
        
    }
    
    public void set${className}Service(${className}Service service) {
        this.${classNameLower}Service = service;
    }
    
}
