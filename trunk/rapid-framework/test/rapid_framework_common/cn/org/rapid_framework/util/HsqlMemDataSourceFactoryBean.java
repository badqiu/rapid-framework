package cn.org.rapid_framework.util;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * FactoryBean用于创建一个hsql内存数据库的DataSource并同时运行初始化的数据库脚本
 * 
 * @author badqiu
 *
 */

public class HsqlMemDataSourceFactoryBean implements FactoryBean{
	private Resource initScriptsLocation;
	
	public HsqlMemDataSourceFactoryBean(){}
	
	public HsqlMemDataSourceFactoryBean(Resource initScriptsLocation) {
		this.initScriptsLocation = initScriptsLocation;
	}

	public void setInitScriptsLocation(Resource initScriptsLocation) {
		this.initScriptsLocation = initScriptsLocation;
	}

	public Object getObject() throws Exception {
		return HsqlDataSourceUtils.getDataSource(initScriptsLocation);
	}

	public Class getObjectType() {
		return DataSource.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
