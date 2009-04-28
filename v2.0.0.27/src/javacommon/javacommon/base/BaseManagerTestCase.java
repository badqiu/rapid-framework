package javacommon.base;


/**
 * @author badqiu
 * 打开注释即可使用Hibernate的MockOpenSessionInViewFilter
 */
public class BaseManagerTestCase extends AbstractDbUnitTransactionalDataSourceSpringContextTests{

//	MockOpenSessionInViewFilter filter = new MockOpenSessionInViewFilter();
	
	public BaseManagerTestCase() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDependencyCheck(false);
		setDefaultRollback(true);
	}
	
	@Override
	protected String[] getConfigLocations() {
		
		fail("此处为故意报错，请先设置好要装载的spring配置文件，如使用默认配置，则删除此行即可。");
		return new String[]{
				"classpath:/spring/*-dao.xml",
				"classpath:/spring/*-service.xml",
				"classpath:/spring/*-resource.xml"};
	}
	
//	@Override
//	protected void onSetUpBeforeTransaction() throws Exception {
//		super.onSetUpBeforeTransaction();
//		
//		//模拟open session in view
//		filter.setSessionFactory(getSessionFactory()); 
//		filter.setSingleSession(true); 
//		filter.beginFilter(); 
//		
//	}
//
//	@Override
//	protected void onTearDownAfterTransaction() throws Exception {
//		super.onTearDownAfterTransaction();
//		filter.endFilter(); 
//	}
	
//	protected SessionFactory getSessionFactory() {
//		SessionFactory sessionFactory = (SessionFactory)applicationContext.getBean("sessionFactory");
//		assertNotNull("not found 'sessionFactory'",sessionFactory);
//		return sessionFactory;
//	}
	
}
