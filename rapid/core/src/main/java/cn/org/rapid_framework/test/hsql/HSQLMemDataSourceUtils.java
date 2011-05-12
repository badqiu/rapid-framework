package cn.org.rapid_framework.test.hsql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
 * sql语句之间的语句使用分号";"分隔
 * @author badqiu
 *
 */
public class HSQLMemDataSourceUtils {

	public static DataSource getDataSource(Class initScripts,String encoding) {
		String resource = "classpath:"+initScripts.getName().replace('.', '/')+".sql";
		try {
			File file = ResourceUtils.getFile(resource);
			return getDataSource(file,encoding);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("sql file not found,file:"+resource,e);
		}
	}
	
	public static DataSource getDataSource(String initScripts) {
		return getDataSource(new StringReader(initScripts));
	}
	
	public static DataSource getDataSource(Resource initScripts,String encoding) {
		Reader input = null;
		try {
			input = new InputStreamReader(initScripts.getInputStream(),encoding);
			return getDataSource(input);
		} catch (Exception e) {
			throw new IllegalStateException("get datasource occer Exception:"+e,e);
		}finally {
			IOUtils.closeQuietly(input);
		}
	}
	
	public static DataSource getDataSource(File initScripts,String encoding) {
		System.out.println("execute hsql db scripts from file:"+initScripts.getAbsolutePath());
		Reader input = null;
		try {
			input = new InputStreamReader(new FileInputStream(initScripts),encoding);
			return getDataSource(input);
		} catch (IOException e) {
			throw new IllegalStateException("get datasource occer IOException:"+e,e);
		}finally {
			IOUtils.closeQuietly(input);
		}
	}
	
	public static DataSource getDataSource(Reader initScripts) {
		DataSource ds = getDataSource();
		
		try {
			executeSqlScripts(initScripts, ds);
		} catch (Exception e) {
			throw new IllegalStateException("execute sql error",e);
		}
		return ds;
	}
	
	static long hsqlDbIdSequence = 0;
	public static synchronized DataSource getDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("org.hsqldb.jdbcDriver");
		ds.setUrl("jdbc:hsqldb:mem:memDB"+System.currentTimeMillis()+""+(hsqlDbIdSequence++));
		ds.setUsername("sa");
		ds.setPassword("");
		return ds;
	}

	public static void executeSqlScripts(Reader initScripts,DataSource ds) throws SQLException, IOException {
		Connection conn = ds.getConnection();
		try {
			String sql = IOUtils.toString(initScripts);
			StringTokenizer tokenizer = new StringTokenizer(sql,";");
			System.out.println("Execute HSQL DB DataSource with sql:");
			while(tokenizer.hasMoreTokens()) {
				String tokenSql = tokenizer.nextToken().trim();
				if("".equals(tokenSql)) {
					continue;
				}
				System.out.println(tokenSql);
				try {
					Statement stat = conn.createStatement();
					stat.execute(tokenSql);
					stat.close();
				}catch(SQLException e) {
					throw new SQLException("execute sql error:"+e+" error sql:\n"+tokenSql+" cause:"+e);
				}
			}
		}finally {
			conn.close();
		}
	}
}
