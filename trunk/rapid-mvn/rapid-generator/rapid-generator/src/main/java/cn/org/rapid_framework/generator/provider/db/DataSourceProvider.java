package cn.org.rapid_framework.generator.provider.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import cn.org.rapid_framework.generator.GeneratorConstants;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.StringHelper;
/**
 * 用于提供生成器的数据源
 * 
 * @author badqiu
 *
 */
public class DataSourceProvider {
	private static Connection connection;
	private static DataSource dataSource;

	public synchronized static Connection getNewConnection() {
		try {
			return getDataSource().getConnection();
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public synchronized static Connection getConnection() {
		try {
			if(connection == null || connection.isClosed()) {
				connection = getDataSource().getConnection();
			}
			return connection;
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void setDataSource(DataSource dataSource) {
		DataSourceProvider.dataSource = dataSource;
	}

	public synchronized static DataSource getDataSource() {
		if(dataSource == null) {
			dataSource = lookupJndiDataSource(GeneratorProperties.getProperty(GeneratorConstants.DATA_SOURCE_JNDI_NAME));
			if(dataSource == null) {
				dataSource = new DriverManagerDataSource(GeneratorProperties.getRequiredProperty(GeneratorConstants.JDBC_URL), 
						GeneratorProperties.getRequiredProperty(GeneratorConstants.JDBC_USERNAME), 
						GeneratorProperties.getProperty(GeneratorConstants.JDBC_PASSWORD), 
						GeneratorProperties.getRequiredProperty(GeneratorConstants.JDBC_DRIVER));
			}
		}
		return dataSource;
	}

	private static DataSource lookupJndiDataSource(String name) {
		if(StringHelper.isBlank(name)) return null;
		
		try {
			Context context = new InitialContext();
			return (DataSource) context.lookup(name);
		}catch(NamingException e) {
			GLogger.warn("lookup generator dataSource fail by name:"+name+" cause:"+e.toString()+",retry by jdbc_url again");
			return null;
		}
	}
	
	public static class DriverManagerDataSource implements DataSource {
		private String url;
		private String username;
		private String password;
		private String driverClass;
		
		private static void loadJdbcDriver(String driver) {
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("not found jdbc driver class:["+driver+"]",e);
			}
		}
		
		public DriverManagerDataSource(String url, String username,String password, String driverClass) {
			this.url = url;
			this.username = username;
			this.password = password;
			this.driverClass = driverClass;
			loadJdbcDriver(driverClass);
		}

		public Connection getConnection() throws SQLException {
			return DriverManager.getConnection(url,username,password);
		}

		public Connection getConnection(String username, String password) throws SQLException {
			return DriverManager.getConnection(url,username,password);
		}

		public PrintWriter getLogWriter() throws SQLException {
			throw new UnsupportedOperationException("getLogWriter");
		}

		public int getLoginTimeout() throws SQLException {
			throw new UnsupportedOperationException("getLoginTimeout");
		}

		public void setLogWriter(PrintWriter out) throws SQLException {
			throw new UnsupportedOperationException("setLogWriter");
		}

		public void setLoginTimeout(int seconds) throws SQLException {
			throw new UnsupportedOperationException("setLoginTimeout");
		}

		public <T> T  unwrap(Class<T> iface) throws SQLException {
			if(iface == null) throw new IllegalArgumentException("Interface argument must not be null");
			if (!DataSource.class.equals(iface)) {
				throw new SQLException("DataSource of type [" + getClass().getName() +
						"] can only be unwrapped as [javax.sql.DataSource], not as [" + iface.getName());
			}
			return (T) this;
		}

		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return DataSource.class.equals(iface);
		}
		
		public String toString() {
			return "DataSource: "+"url="+url+" username="+username;
		}

	}
}
