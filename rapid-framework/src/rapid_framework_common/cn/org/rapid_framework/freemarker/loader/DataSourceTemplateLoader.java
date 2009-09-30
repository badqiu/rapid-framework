package cn.org.rapid_framework.freemarker.loader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import freemarker.cache.TemplateLoader;

/**
 * 用于freemarker从数据库装载template文件
 * @author badqiu
 *
 */
public class DataSourceTemplateLoader implements TemplateLoader,InitializingBean{
	Log log = LogFactory.getLog(DataSourceTemplateLoader.class);
	
	private JdbcTemplate jdbcTemplate;
	
    private String tableName;
    private String keyColumn;
    private String templateContentColumn;
    private String timestampColumn;

	public void setDataSource(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}

	public void setTemplateContentColumn(String templateContentColumn) {
		this.templateContentColumn = templateContentColumn;
	}

	public void setTimestampColumn(String timestampColumn) {
		this.timestampColumn = timestampColumn;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(jdbcTemplate,"'dataSource' must be not null");
		Assert.notNull(tableName,"'tableName' must be not null");
		Assert.notNull(keyColumn,"'keyColumn' must be not null");
		Assert.notNull(templateContentColumn,"'templateContentColumn' must be not null");
		log.info("freemarker template load sql:"+getSql(templateContentColumn));
	}

	public Object findTemplateSource(final String name) throws IOException {
		int count = getJdbcTemplate().queryForInt(getSql("count(*)"),new Object[]{name});
		return count > 0 ? name : null;
	}

	private String getSql(String columnNames) {
		return "select "+columnNames+" from "+tableName+" where "+keyColumn+"=?";
	}

	public Reader getReader(Object templateSource, final String encoding) throws IOException {
		final String templateName = (String)templateSource;
		return (Reader)getJdbcTemplate().query(getSql(templateContentColumn),new Object[]{templateName}, new ResultSetExtractor() {
			public Object extractData(ResultSet rs) throws SQLException,DataAccessException {
				while(rs.next()) {
					try {
						return new InputStreamReader(rs.getBinaryStream(templateContentColumn),encoding);
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException("load template from dataSource with templateName:"+templateName+" occer UnsupportedEncodingException",e);
					}
				}
				throw new RuntimeException("not found template from dataSource with templateName:"+templateName);
			}
		});
	}

	public long getLastModified(Object templateSource) {
		if(StringUtils.hasText(timestampColumn)) {
			String templateName = (String)templateSource;
	    	return getJdbcTemplate().queryForLong(getSql(timestampColumn),new Object[]{templateName});
		}
		return -1;
	}

	public void closeTemplateSource(Object templateSource) throws IOException {
	}

}
