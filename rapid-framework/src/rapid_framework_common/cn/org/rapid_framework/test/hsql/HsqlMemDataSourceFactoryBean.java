package cn.org.rapid_framework.test.hsql;

import java.nio.charset.Charset;

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
	private String encoding = Charset.defaultCharset().name();
	
	public HsqlMemDataSourceFactoryBean(){}
	
	public HsqlMemDataSourceFactoryBean(Resource initScriptsLocation,String encoding) {
		this.initScriptsLocation = initScriptsLocation;
		this.encoding = encoding;
	}

	public void setInitScriptsLocation(Resource initScriptsLocation) {
		this.initScriptsLocation = initScriptsLocation;
	}
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Object getObject() throws Exception {
		if(initScriptsLocation == null) {
			return HsqlMemDataSourceUtils.getDataSource();
		}else {
			return HsqlMemDataSourceUtils.getDataSource(initScriptsLocation,encoding);
		}
	}

	public Class getObjectType() {
		return DataSource.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
