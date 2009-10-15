package cn.org.rapid_framework.util;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

public class HsqlDataSourceFactoryBean implements FactoryBean{
	private Resource initScriptsLocation;
	
	public HsqlDataSourceFactoryBean(){}
	
	public HsqlDataSourceFactoryBean(Resource initScriptsLocation) {
		this.initScriptsLocation = initScriptsLocation;
	}

	public void setInitScriptsLocation(Resource initScriptsLocation) {
		this.initScriptsLocation = initScriptsLocation;
	}

	@Override
	public Object getObject() throws Exception {
		return HsqlDataSourceUtils.getDataSource(initScriptsLocation);
	}

	@Override
	public Class getObjectType() {
		return DataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
