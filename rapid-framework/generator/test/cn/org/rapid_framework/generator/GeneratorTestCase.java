package cn.org.rapid_framework.generator;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;

import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import junit.framework.TestCase;

public class GeneratorTestCase extends TestCase{
	
	public void setUp()throws Exception {
		runScripts();
	}

	private void runScripts() throws SQLException, IOException {
		GeneratorProperties.setProperty("jdbc.url", "jdbc:hsqldb:mem:generatorDB");
		GeneratorProperties.setProperty("jdbc.driver", "org.hsqldb.jdbcDriver");
		GeneratorProperties.setProperty("jdbc.username", "sa");
		GeneratorProperties.setProperty("jdbc.password", "");
		Connection conn = DbTableFactory.getInstance().getConnection();
		Connection conn2 = DbTableFactory.getInstance().getConnection();
		assertEquals(conn,conn2);
		
		Statement stat = conn.createStatement();
		String sqlTables = FileUtils.readFileToString(new File("generator/test/generator_test_table.sql"), "UTF-8");
		System.out.println(sqlTables);
		stat.execute(sqlTables);
		stat.close();
	}
}
