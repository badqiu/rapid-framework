<#include '/java_copyright.include'/>
 
package ${tableConfig.basepackage}.ibatis;

import ${tableConfig.basepackage}.operation.${tableConfig.className?lower_case}.*;

<#include '/java_import.include'/>

import org.springframework.dao.DataAccessException;

import ${tableConfig.basepackage}.dataobject.${tableConfig.className}DO;
import ${tableConfig.basepackage}.daointerface.${tableConfig.className}DAO;

import org.nuxeo.runtime.test.autowire.annotation.XAutoWire;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ${tableConfig.className}DAOTest
 */
public class Ibatis${tableConfig.className}DAOTest extends BaseDaoTestCase {
	@XAutoWire(XAutoWire.BY_NAME)
	protected Ibatis${tableConfig.className}DAO ${tableConfig.className}DAO;

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }
        	
<#list tableConfig.sqls as sql>
	public void test_${sql.operation}() {
		
	}
</#list>

}



