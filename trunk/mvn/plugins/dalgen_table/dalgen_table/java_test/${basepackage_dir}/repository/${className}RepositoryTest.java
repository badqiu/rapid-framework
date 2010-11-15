<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import junit.framework.TestCase;
import static junit.framework.Assert.*;

<#include "/java_imports.include">

public class ${className}RepositoryTest extends TestCase{
	
	private ${className}Repository repository;
	
	public void test_find_some_thing() {
	    
	}
	
	public void set${className}Repository(${className}Repository repository) {
	    this.repository = repository;
	}
}
