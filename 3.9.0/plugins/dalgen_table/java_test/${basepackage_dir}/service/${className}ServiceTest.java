<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import junit.framework.TestCase;

<#include "/java_imports.include">
public class ${className}ServiceTest extends TestCase{

    public void test_create${className}() {
        
    }
    
}
