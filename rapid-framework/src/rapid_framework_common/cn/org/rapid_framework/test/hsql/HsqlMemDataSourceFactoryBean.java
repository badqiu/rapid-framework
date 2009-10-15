package cn.org.rapid_framework.test.hsql;

import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * FactoryBean用于创建一个hsql内存数据库的DataSource并同时运行初始化的数据库脚本
 * 
 *  sql语句之间的语句使用分号";"分隔
 * 
 * @author badqiu
 *
 */

public class HsqlMemDataSourceFactoryBean implements FactoryBean{
	private Resource[] initScriptLocations;
	private String encoding = Charset.defaultCharset().name();
	
	public HsqlMemDataSourceFactoryBean(){}
	
	public HsqlMemDataSourceFactoryBean(Resource initScriptsLocation,String encoding) {
		this.initScriptLocations = new Resource[] {initScriptsLocation};
		this.encoding = encoding;
	}

	public void setInitScriptLocations(Resource... initScriptsLocation) {
		this.initScriptLocations = initScriptsLocation;
	}
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Object getObject() throws Exception {
		DataSource ds = HsqlMemDataSourceUtils.getDataSource();
		if(initScriptLocations != null) {
			for(Resource r : initScriptLocations) {
				HsqlMemDataSourceUtils.runDataSourceWithScripts(new InputStreamReader(r.getInputStream(),encoding), ds);
			}
		}
		return ds;
	}

	public Class getObjectType() {
		return DataSource.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
