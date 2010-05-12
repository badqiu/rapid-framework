<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import static junit.framework.Assert.*;

<#include "/java_imports.include">

public class ${className}RepositoryImpl extends BaseRepository {
	
	public void update();
	
	public void create();
	
	public void removeById();
	
	public void queryById();
	
	public void findPage();
	
}
