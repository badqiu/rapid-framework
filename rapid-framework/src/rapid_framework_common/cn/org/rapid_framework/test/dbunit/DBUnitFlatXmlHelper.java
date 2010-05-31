package cn.org.rapid_framework.test.dbunit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.util.ResourceUtils;

/**
 * 数据库测试有用的类,用于装载Dbunit测试数据,恢复测试数据
 * @author badqiu1
 *
 */
public class DBUnitFlatXmlHelper {
	private DatabaseConnection databaseConnection; // DbUnit的数据库连接

	private CachedDataSet cachedDataSet; // 存储临时数据

	private DataSource dataSource; //数据源

	private List testDataSets = new ArrayList(); //插入数据库的临时数据
	
	private String jdbcSchema = null;
	
	public DBUnitFlatXmlHelper() {
	}
	
	public DBUnitFlatXmlHelper(DataSource d) throws Exception {
		this(d,null);
	}
	
	public DBUnitFlatXmlHelper(DataSource dataSource, String jdbcSchema) throws Exception {
		this.jdbcSchema = jdbcSchema;
		setDataSource(dataSource);
	}

	public void setJdbcSchema(String jdbcSchema) {
		this.jdbcSchema = jdbcSchema;
	}
	
	public void insertTestDatas(String... flatXmlDataFiles) throws DatabaseUnitException, SQLException, IOException {
		if(flatXmlDataFiles == null) return;
		
		for (String dataFile : flatXmlDataFiles) {
			try {
				File file = ResourceUtils.getFile(dataFile);
				System.out.println("[DbUnit INFO] insert test dataFile,filepath="+dataFile);
				insertTestData(file);
			} catch (FileNotFoundException e) {
				System.out.println("[DbUnit WARN] not found DbUnit DataFile,filepath="+ dataFile);
				continue;
			}
		}
	}
	
	/**
	 * 插入测试数据,数据格式为DBUnit的flatXMLFile,
	 * 
	 * 如:<BR>
	 * &lt;dataset&gt;<BR>
	 * &nbsp;&nbsp;&nbsp;&lt;TABLE_NAME field1="username1" <BR>
	 * &nbsp;&nbsp;&nbsp;			field2="pwd1"/&gt; <BR>
	 * &nbsp;&nbsp;&nbsp;&lt;TABLE_NAME field2="password" <BR>
	 * &nbsp;&nbsp;&nbsp;			field1="username2"/&gt;<BR>
	 * &lt;/dataset&gt;<BR>
	 * USER为表名,username,password为字段名
	 * @param flatXMLFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DatabaseUnitException
	 * @throws SQLException
	 * @link http://dbunit.sourceforge.net/components.html#dataset
	 */
	public void insertTestData(File flatXMLFile) throws FileNotFoundException, IOException, DatabaseUnitException, SQLException {
	    try {
		IDataSet testDataSet = new FlatXmlDataSet(new FileInputStream(flatXMLFile));
		DatabaseOperation.REFRESH.execute(getDatabaseConnection(),testDataSet);
		testDataSets.add(testDataSet);
	    }catch(AmbiguousTableNameException e) {
	        throw new DatabaseUnitException("出现AmbiguousTableNameException异常，使用命令:purge recyclebin清空一下oracle回收站,并为dbunit指定jdbcSchema",e);
	    }
	}

	/**删除测试数据*/
	public void deleteTestDatas() throws DatabaseUnitException, SQLException {
		System.out.println("[DbUnit INFO] delete test datas");
		for(ListIterator it = testDataSets.listIterator(); it.hasNext();) {
			IDataSet testDataSet = (IDataSet)it.next();
			DatabaseOperation.DELETE.execute(getDatabaseConnection(),testDataSet);
			it.remove();
		}
		testDataSets.clear();
	}
	
	/**
	 * 备份单个表
	 * 
	 * @param tableName
	 * @throws Exception
	 */
	public void backupTable(String tableName) throws Exception {
		String[] tableNames = { tableName };
		backupTables(tableNames);
	}

	/**
	 * 备份数据库中相关的表
	 * 
	 * @param tableNames
	 *            数据库中的表名
	 */
	public void backupTables(String[] tableNames) throws Exception {
		cachedDataSet = new CachedDataSet(getDatabaseConnection().createDataSet(
				tableNames));
	}
	
	/**
	 * 恢复数据至前一备份状态，这里执行删除后添加操作
	 */
	public void restoreTables() throws Exception {
		deleteTestDatas();
		if(cachedDataSet != null) {
			DatabaseOperation.CLEAN_INSERT.execute(getDatabaseConnection(),cachedDataSet);
			cachedDataSet = null;
		}
	}

	/**
	 * 将多张表的数据根据多个query导出到文件,文件格式为flatXMLFile<BR>
	 * 表名与query的index相对应
	 * 
	 * @param tableName 表名
	 * @param sqlQuery SQL查询语句
	 * @param flatXMLFile 导出的文件
	 * @throws DataSetException 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void exportFromDBToFile(String tableName[],String sqlQuery[],File flatXMLFile) throws DataSetException, FileNotFoundException, SQLException, IOException {
		if(tableName.length != sqlQuery.length)
			throw new RuntimeException("tableName.length="+tableName.length+" != query.length="+sqlQuery.length);
		for(int i = 0; i < tableName.length; i++) {
			exportFromDBToFile(tableName,sqlQuery,flatXMLFile);
		}
	}
	/**
	 * 将表的数据根据query导出到文件,文件格式为flatXMLFile
	 * 
	 * @param tableName 表名
	 * @param sqlQuery SQL查询语句
	 * @param flatXMLFile 导出的文件
	 * @throws DataSetException 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void exportFromDBToFile(String tableName,String sqlQuery,File flatXMLFile) throws DataSetException, FileNotFoundException, SQLException, IOException {
		QueryDataSet partialDataSet = new QueryDataSet(getDatabaseConnection());
		partialDataSet.addTable(tableName,sqlQuery);
		FlatXmlDataSet.write(partialDataSet, new FileOutputStream(flatXMLFile));
	}
	
	/**
	 * 将单个表的数据导出为一个DBUnit的FlatXMLFile
	 * 
	 * @see insertTestData()
	 * @param tableNames 需要导入数据的表名s
	 * @param flatXMLFile 导出的文件
	 * @throws SQLException
	 * @throws DataSetException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */	
	public void exportFromDBToFile(String tableName,File flatXMLFile) throws DataSetException, FileNotFoundException, SQLException, IOException {
		exportFromDBToFile(new String[]{tableName},flatXMLFile);
	}
	
	/**
	 * 将多张表的数据导出为一个DBUnit的FlatXMLFile
	 * 
	 * @see insertTestData()
	 * @param tableNames 需要导入数据的表名s
	 * @param flatXMLFile 导出的文件
	 * @throws SQLException
	 * @throws DataSetException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */	
	public void exportFromDBToFile(String[] tableNames,File flatXMLFile) throws SQLException, DataSetException, FileNotFoundException, IOException {
		QueryDataSet partialDataSet = new QueryDataSet(getDatabaseConnection());
		for(int i = 0; i < tableNames.length; i++ ) {
			partialDataSet.addTable(tableNames[i]);
		}
		FlatXmlDataSet.write(partialDataSet, new FileOutputStream(flatXMLFile));
	}
	
	/**
	 * 设置数据源
	 * 
	 * @param d
	 * @throws Exception
	 */
	public void setDataSource(DataSource d) throws Exception {
		this.dataSource = d;
		initDbUnitConnection();
	}
	
	public void setDataSource(DataSource d,String jdbcSchema) throws Exception {
		setJdbcSchema(jdbcSchema);
		setDataSource(d);
	}
	
	public void setDatabaseConnection(DatabaseConnection databaseConnection) {
		this.databaseConnection = databaseConnection;
	}

	/**
	 * 释放数据库连接资源
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		if (databaseConnection != null)
			databaseConnection.close();
	}

	/**
	 * 初始化DbUnit的数据库连接
	 * 
	 * @throws Exception
	 *             数据库连接出错
	 */
	private void initDbUnitConnection() throws Exception {
		if (dataSource != null)
			databaseConnection = (new DatabaseConnection(dataSource.getConnection(),jdbcSchema));
	}

	private DatabaseConnection getDatabaseConnection() {
		if (databaseConnection == null)
			throw new RuntimeException("请先设置数据源,执行setDataSource()");
		return databaseConnection;
	}

}

