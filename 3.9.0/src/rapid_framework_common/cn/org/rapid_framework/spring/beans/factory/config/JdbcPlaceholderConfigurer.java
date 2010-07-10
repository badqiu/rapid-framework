package cn.org.rapid_framework.spring.beans.factory.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.Assert;

/**
 * 通过数据库解析spring placeholder的值,配置示例
 * 
 * 
 * <pre>
 * 	&lt;bean id="propertyConfigurer" class="cn.org.rapid_framework.spring.beans.factory.config.JdbcPlaceholderConfigurer">
 *		&lt;property name="dataSource" ref="dataSource"/>
 *		&lt;property name="sql" value="select config_value,[default_value] from t_app_config where config_key = ?"/>
 *	&lt;/bean>
 *	</pre>
 * [default_value]列可选
 * ?号为传递的placeholder的值
 * @author badqiu
 *
 */
public class JdbcPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements InitializingBean{
	private DataSource dataSource;
	private String sql;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(dataSource,"'dataSource' property must be not null");
		Assert.hasText(sql,"'sql' property must be not empty");
		logger.info("spring resolve placeholder sql:"+sql);
	}

	protected String resolvePlaceholder(String placeholder, Properties props,int systemPropertiesMode) {
		String value = super.resolvePlaceholder(placeholder, props, systemPropertiesMode);
		if(value == null) {
			try {
				value = resolveJdbcPlaceholder(placeholder);
			}catch(RuntimeException e) {
				throw new BeanDefinitionStoreException("resolve placeholder by jdbc sql error,placeholder="+placeholder,e);
			}
		}
		return value;
	}

	private String resolveJdbcPlaceholder(String placeholder) {
		SqlRowSet rs = new JdbcTemplate(dataSource).queryForRowSet(sql,new Object[]{placeholder});
		int columnCount = rs.getMetaData().getColumnCount();
		Assert.isTrue(columnCount == 1 || columnCount == 2,"error resultSet columnCount,sql example: select config_value,[default_value] from app_config where config_key=?,[default_value] is option");
		if(!rs.next()) 
			return null;
		
		switch(columnCount) {
		case 1:
			return rs.getString(1);
		case 2:
			String value = rs.getString(1);
			String defaultValue = rs.getString(2);
			if(value == null) {
				value = defaultValue;
			}
			return value;
		default:
				throw new IllegalArgumentException("error columnCount:"+columnCount+", sql:"+sql);
		}
	}



}
