package javacommon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.util.Assert;

public class SqlSessionFactoryFactoryBean implements FactoryBean,InitializingBean{
	private Resource configurationFile;
	private DataSource dataSource;
	private boolean useTransactionAwareDataSource = true;
	
	SqlSessionFactory sqlSessionFactory;
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(configurationFile,"configurationFile must be not null");
		
		sqlSessionFactory = createSqlSessionFactory();
	}

	private SqlSessionFactory createSqlSessionFactory() throws IOException {
		Reader reader = new InputStreamReader(getConfigurationFile().getInputStream());
		try {
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			if(dataSource != null) {
				DataSource dataSourceToUse = this.dataSource; 
				if (this.useTransactionAwareDataSource  && !(this.dataSource instanceof TransactionAwareDataSourceProxy)) {  
		            dataSourceToUse = new TransactionAwareDataSourceProxy(this.dataSource);  
		        }
				
				Configuration conf = sqlSessionFactory.getConfiguration();
				conf.setEnvironment(new Environment("development",new ManagedTransactionFactory(),dataSourceToUse));
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(conf);
			}
			return sqlSessionFactory;
		}finally {
			reader.close();
		}
	}
	
	public Object getObject() throws Exception {
		return sqlSessionFactory;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Class getObjectType() {
		return SqlSessionFactory.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public Resource getConfigurationFile() {
		return configurationFile;
	}

	public void setConfigurationFile(Resource configurationFile) {
		this.configurationFile = configurationFile;
	}

}
