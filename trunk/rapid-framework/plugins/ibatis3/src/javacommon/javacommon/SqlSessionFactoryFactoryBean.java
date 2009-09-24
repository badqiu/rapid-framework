package javacommon;

import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class SqlSessionFactoryFactoryBean implements FactoryBean,InitializingBean{
	private Resource configurationFile;
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(configurationFile,"configurationFile must be not null");
	}
	
	public Object getObject() throws Exception {
		Reader reader = new InputStreamReader(getConfigurationFile().getInputStream());
		try {
			SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
			return sessionFactory;
		}finally {
			reader.close();
		}
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
