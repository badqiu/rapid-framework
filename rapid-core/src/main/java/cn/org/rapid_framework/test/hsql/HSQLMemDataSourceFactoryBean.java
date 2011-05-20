package cn.org.rapid_framework.test.hsql;

import java.io.InputStreamReader;
import java.io.StringReader;
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

public class HSQLMemDataSourceFactoryBean implements FactoryBean{
	private Resource[] scriptLocations;
	private String sqlScript;
	private String encoding = Charset.defaultCharset().name();
	
	public HSQLMemDataSourceFactoryBean(){}
	
	public HSQLMemDataSourceFactoryBean(Resource initScriptsLocation,String encoding) {
		this.scriptLocations = new Resource[] {initScriptsLocation};
		this.encoding = encoding;
	}

	public void setScriptLocations(Resource... initScriptsLocation) {
		this.scriptLocations = initScriptsLocation;
	}
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public void setSqlScript(String sqlScript) {
		this.sqlScript = sqlScript;
	}

	public Object getObject() throws Exception {
		DataSource ds = HSQLMemDataSourceUtils.getDataSource();
		if(scriptLocations != null) {
			for(Resource r : scriptLocations) {
				HSQLMemDataSourceUtils.executeSqlScripts(new InputStreamReader(r.getInputStream(),encoding), ds);
			}
		}
		if(sqlScript != null) {
			HSQLMemDataSourceUtils.executeSqlScripts(new StringReader(sqlScript), ds);
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
