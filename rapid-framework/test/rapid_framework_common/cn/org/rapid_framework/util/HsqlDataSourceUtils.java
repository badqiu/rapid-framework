package cn.org.rapid_framework.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.ResourceUtils;
/**
 * 创建hsql DataSource的同时运行初始化的数据库脚本
 * 
 * @author badqiu
 *
 */
public class HsqlDataSourceUtils {

	public static DataSource getDataSource(Class initScripts) {
		try {
			File file = ResourceUtils.getFile(initScripts.getClass().getName().replace('.', '/'));
			return getDataSource(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("execute sql error",e);
		}
	}
	
	public static DataSource getDataSource(String initScripts) {
		return getDataSource(new StringReader(initScripts));
	}
	
	public static DataSource getDataSource(File initScripts) {
		FileReader input = null;
		try {
			input = new FileReader(initScripts);
			return getDataSource(input);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("execute sql error",e);
		}finally {
			IOUtils.closeQuietly(input);
		}
	}
	
	public static DataSource getDataSource(Reader initScripts) {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("org.hsqldb.jdbcDriver");
		ds.setUrl("jdbc:hsqldb:mem:memDB");
		ds.setUsername("sa");
		ds.setPassword("");
		
		try {
			runDataSourceWithScripts(initScripts, ds);
		} catch (Exception e) {
			throw new RuntimeException("execute sql error",e);
		}
		return ds;
	}

	private static void runDataSourceWithScripts(Reader initScripts,DriverManagerDataSource ds) throws SQLException, IOException {
		Connection conn = ds.getConnection();
		Statement stat = conn.createStatement();
		try {
			String sql = IOUtils.toString(initScripts);
			System.out.println("init hsql db with sql:"+sql);
			stat.execute(sql);
			stat.close();
		}finally {
			stat.close();
			conn.close();
		}
	}
}
