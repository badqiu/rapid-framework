package javacommon.base;

import java.io.FileNotFoundException;



/**
 * @author badqiu
 */
public class BaseDaoTestCase extends AbstractDbUnitTransactionalDataSourceSpringContextTests {
	
	public BaseDaoTestCase() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDependencyCheck(false);
		setDefaultRollback(true);
	}
	
	@Override
	protected String[] getConfigLocations() {
		fail("此处为故意报错，请先设置好要装载的spring配置文件，如使用默认配置，则删除此行即可。");
		return new String[]{
				"classpath:/spring/*-dao.xml",
				"classpath:/spring/*-resource.xml"};
	}
	
}
