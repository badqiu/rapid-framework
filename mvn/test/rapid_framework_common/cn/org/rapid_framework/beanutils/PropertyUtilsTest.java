package cn.org.rapid_framework.beanutils;

import junit.framework.TestCase;
import cn.org.rapid_framework.util.fortest.Role;
/**
 * 
 * @author pengtaoli
 *
 */
public class PropertyUtilsTest extends TestCase {
	private Role role;
	
	@Override
	protected void setUp(){
		role = new Role();
	}
	
    public void testSetProperty(){
    	role.setRoleId(new Long(1));
    	assertEquals(role.getRoleId(), new Long(1));
    	
		PropertyUtils.setProperty(role, "roleId", new Long(2));
		assertEquals(role.getRoleId(),Long.valueOf(2));
    }
}
