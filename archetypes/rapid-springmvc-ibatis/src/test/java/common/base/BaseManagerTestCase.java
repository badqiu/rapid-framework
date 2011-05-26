package common.base;
import static junit.framework.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.util.ResourceUtils;

import cn.org.rapid_framework.test.context.TestMethodContextExecutionListener;
import cn.org.rapid_framework.test.dbunit.DBUnitFlatXmlHelper;


/**
 * 
 * 本基类主要为子类指定好要装载的spring配置文件
 * 及在运行测试前通过dbunit插入测试数据在数据库中,运行完测试删除测试数据
 *
 * @author badqiu
 * 请设置好要装载的spring配置文件,一般开发数据库与测试数据库分开
 * 所以你要装载的资源文件应改为"classpath:/spring/*-test-resource.xml"
 */
@ContextConfiguration(locations={"classpath:/spring/*-resource.xml",
								 "classpath:/spring/*-validator.xml",
                                 "classpath:/spring/*-datasource.xml",
                                 "classpath:/spring/*-dao.xml",
                                 "classpath:/spring/*-service.xml"})
@TestExecutionListeners(listeners = TestMethodContextExecutionListener.class) // TestMethodContextExecutionListener用于在@Before时可以得到测试方法名称                                 
public class BaseManagerTestCase extends AbstractTransactionalJUnit4SpringContextTests{

	protected DBUnitFlatXmlHelper dbUnitHelper = new DBUnitFlatXmlHelper();

	protected DataSource getDataSource() {
		DataSource ds = (DataSource)applicationContext.getBean("dataSource");
		assertNotNull("not found 'dataSource'",ds);
		return ds;
	}
	
	@Before
	public void onSetUpInsertTestDatas() throws Exception {
		String jdbcSchema = null;  // set schema for oracle,出现AmbiguousTableNameException时，使用命令:purge recyclebin清空一下oracle回收站
		dbUnitHelper.setDataSource(getDataSource(),jdbcSchema);
		dbUnitHelper.insertTestDatas(getDbUnitDataFiles());
	}
	
	/** 得到要加载的dbunit文件 */
	protected String[] getDbUnitDataFiles() {
	    return null;
	}
	
    protected void insertTestData(String classpathFileName) {
        try {
            dbUnitHelper.insertTestData(ResourceUtils.getFile("classpath:"+classpathFileName));
        }catch(Exception e) {
            throw new RuntimeException("insertTestData error,classpathFileName:"+classpathFileName,e);
        }
    }
    
}
