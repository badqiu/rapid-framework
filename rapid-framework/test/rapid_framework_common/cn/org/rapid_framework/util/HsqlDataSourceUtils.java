package cn.org.rapid_framework.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.ResourceUtils;
/**
 * 创建一个hsql内存数据库的DataSource并同时运行初始化的数据库脚本
 * 
 * @author badqiu
 *
 */
public class HsqlDataSourceUtils {

	public static DataSource getDataSource(Class initScripts) {
		try {
			File file = ResourceUtils.getFile("classpath:"+initScripts.getName().replace('.', '/')+".sql");
			return getDataSource(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("execute sql error",e);
		}
	}
	
	public static DataSource getDataSource(String initScripts) {
		return getDataSource(new StringReader(initScripts));
	}
	
	public static DataSource getDataSource(Resource initScripts) {
		Reader input = null;
		try {
			input = new InputStreamReader(initScripts.getInputStream());
			return getDataSource(input);
		} catch (Exception e) {
			throw new RuntimeException("execute sql error",e);
		}finally {
			IOUtils.closeQuietly(input);
		}
	}
	
	public static DataSource getDataSource(File initScripts) {
		System.out.println("execute hsql db scripts from file:"+initScripts.getAbsolutePath());
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
		try {
			String sql = IOUtils.toString(initScripts);
			StringTokenizer tokenizer = new StringTokenizer(sql,";");
			System.out.println("Init hsql db with sql:");
			while(tokenizer.hasMoreTokens()) {
				String tokenSql = tokenizer.nextToken();
				if("".equals(tokenSql.trim())) {
					continue;
				}
				System.out.println(tokenSql);
				Statement stat = conn.createStatement();
				stat.execute(tokenSql);
				stat.close();
			}
			
//			Statement stat = conn.createStatement();
//			stat.execute(sql);
//			stat.close();
		}finally {
			conn.close();
		}
	}
}
