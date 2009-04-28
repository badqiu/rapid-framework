package javacommon.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javacommon.util.DBUnitUtils;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.util.ResourceUtils;
/**
 * 将spring与DbUnit整合的数据库测试基类
 * @author badqiu
 *
 */
public abstract class AbstractDbUnitTransactionalDataSourceSpringContextTests
		extends AbstractTransactionalDataSourceSpringContextTests {

	protected DBUnitUtils dbUnitUtils = new DBUnitUtils();



	protected DataSource getDataSource() {
		DataSource ds = (DataSource)applicationContext.getBean("dataSource");
		assertNotNull("not found 'dataSource'",ds);
		return ds;
	}

	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		super.onSetUpBeforeTransaction();
		dbUnitUtils.setDataSource(getDataSource(),null);
		dbUnitUtils.insertDbunitTestDatas(getDbUnitDataFiles());
	}

	@Override
	protected void onTearDownAfterTransaction() throws Exception {
		super.onTearDownAfterTransaction();
		System.out.println("[DbUnit INFO] delete test datas");
		dbUnitUtils.deleteTestDatas();
	}

	protected String[] getDbUnitDataFiles() {
		return new String[]{};
	}

}