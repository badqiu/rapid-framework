package cn.org.rapid_framework.generator;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorModelUtils;
import cn.org.rapid_framework.generator.provider.db.DataSourceProvider;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.IOHelper;

public class GeneratorTestCase extends TestCase{
	protected Generator g = new Generator();;
	private static boolean notRunCreatedTabled = true;
	public void setUp()throws Exception {
		GLogger.logLevel = GLogger.DEBUG;
	    System.setProperty("gg.isOverride", "true");
		
	    if(notRunCreatedTabled) {
	    	runSqlScripts();
	    	notRunCreatedTabled = false;
	    }
		
		if(isRuningByAnt()) {
			String tempDir = getTempDir();
			System.out.println("running by ant, set outRootDir to tempDir="+tempDir);
			g.setOutRootDir(tempDir);
		}else {
			if(g.getOutRootDir() == null)
				g.setOutRootDir(".");
		}
	}

	public boolean isRuningByAnt() {
		return System.getProperty("java.class.path").indexOf("ant.jar") >= 0;
	}

	public static void runSqlScripts() throws SQLException, IOException {
		GeneratorProperties.setProperty("jdbc.url", "jdbc:hsqldb:mem:generatorDB");
		GeneratorProperties.setProperty("jdbc.driver", "org.hsqldb.jdbcDriver");
		GeneratorProperties.setProperty("jdbc.username", "sa");
		GeneratorProperties.setProperty("jdbc.password", "");
		GeneratorProperties.setProperty("jdbc.schema", "");
		GeneratorProperties.setProperty("jdbc.catalog", "");
		
		runSqlScripts("generator/test/generator_test_table.sql");
		
	}

	public static void runSqlScripts(String file) throws SQLException, IOException {
		Connection conn = DataSourceProvider.getConnection();
		Connection conn2 = DataSourceProvider.getConnection();
		assertEquals(conn,conn2);
		
//		System.out.println(conn.getCatalog());
		
		Statement stat = conn.createStatement();
		String sqlTables = IOHelper.readFile(new File(file));
		System.out.println(sqlTables);
		for(String t : sqlTables.trim().split(";")) {
			stat.execute(t.trim());
		}
		stat.close();
	}
	
	public void generateByTable(Table table) throws Exception {
		GeneratorModel m = GeneratorModelUtils.newFromTable(table);
//		g.setIgnoreTemplateGenerateException(false);
		g.generateBy(m.templateModel, m.filePathModel);
	}
	
	public void generateByTable(Generator g,Table table) throws Exception {
		GeneratorModel m = GeneratorModelUtils.newFromTable(table);
		g.generateBy(m.templateModel, m.filePathModel);
	}
	
	public String getTempDir() {
		String tempDir = System.getProperty("java.io.tmpdir");
		return tempDir+"/test_generator_out";
	}
	
}
