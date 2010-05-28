package cn.org.rapid_framework.generator.provider.db;


import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.util.ResourceUtils;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.provider.db.model.Column;
import cn.org.rapid_framework.generator.provider.db.model.Table;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.XMLHelper;
import cn.org.rapid_framework.generator.util.XMLHelper.NodeData;
/**
 * 
 * @author badqiu
 * @email badqiu(a)gmail.com
 */
public class DbTableFactory {

	private DbHelper dbHelper = new DbHelper();
	private Connection connection;
	private static DbTableFactory instance = null;
	
	private DbTableFactory() {
		init();
	}

	private void init() {
		String driver = GeneratorProperties.getRequiredProperty("jdbc.driver");
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("not found jdbc driver class:["+driver+"]",e);
		}
	}
	
	public synchronized static DbTableFactory getInstance() {
		if(instance == null) instance = new DbTableFactory();
		return instance;
	}
	
	public String getCatalog() {
		return GeneratorProperties.getNullIfBlank("jdbc.catalog");
	}

	public String getSchema() {
		return GeneratorProperties.getNullIfBlank("jdbc.schema");
	}

	public Connection getConnection() throws SQLException {
		if(connection == null || connection.isClosed()) {
			connection = DriverManager.getConnection(GeneratorProperties.getRequiredProperty("jdbc.url"),GeneratorProperties.getRequiredProperty("jdbc.username"),GeneratorProperties.getProperty("jdbc.password"));
		}
		return connection;
	}

	public List getAllTables() throws Exception {
		Connection conn = getConnection();
		return getAllTables(conn);
	}
	
	public Table getTable(String sqlTableName) throws Exception {
		Table t = _getTable(sqlTableName);
		if(t == null && !sqlTableName.equals(sqlTableName.toUpperCase())) {
			t = _getTable(sqlTableName.toUpperCase());
		}
		if(t == null && !sqlTableName.equals(sqlTableName.toLowerCase())) {
			t = _getTable(sqlTableName.toLowerCase());
		}
		
		if(t == null) {
			throw new RuntimeException("not found table with give name:"+sqlTableName);
		}
		return t;
	}

	private Map getTableConfigMap(String tableSqlName){
		NodeData nd = getTableConfigXml(tableSqlName);
		return nd == null ? new HashMap() : nd.getElementMap("sqlName");
	}

	private NodeData getTableConfigXml(String tableSqlName){
		try {
			File file = ResourceUtils.getFile("classpath:"+tableSqlName+".xml");
			GLogger.debug("getTableConfigXml() load nodeData by tableSqlName:"+tableSqlName+".xml");
			return new XMLHelper().parseXML(file);
		}catch(Exception e) {//ignore
			GLogger.info("getTableConfigMap exception:"+e);
			return null;
		}
	}

	private Table _getTable(String sqlTableName) throws SQLException {
		Connection conn = getConnection();
		DatabaseMetaData dbMetaData = conn.getMetaData();
		ResultSet rs = dbMetaData.getTables(getCatalog(), getSchema(), sqlTableName, null);
		while(rs.next()) {
			Table table = createTable(conn, rs);
			return table;
		}
		return null;
	}

	private Table createTable(Connection conn, ResultSet rs) throws SQLException {
		String realTableName = null;
		try {
			ResultSetMetaData rsMetaData = rs.getMetaData();
			String schemaName = rs.getString("TABLE_SCHEM") == null ? "" : rs.getString("TABLE_SCHEM");
			realTableName = rs.getString("TABLE_NAME");
			String tableType = rs.getString("TABLE_TYPE");
			String remarks = rs.getString("REMARKS");
			if(remarks == null && dbHelper.isOracleDataBase()) {
				remarks = getOracleTableComments(realTableName);
			}
			
			Table table = new Table();
			table.setSqlName(realTableName);
			table.setRemarks(remarks);
			
			if ("SYNONYM".equals(tableType) && dbHelper.isOracleDataBase()) {
			    table.setOwnerSynonymName(getSynonymOwner(realTableName));
			}
			
			retriveTableColumns(table);
			
			table.initExportedKeys(conn.getMetaData());
			table.initImportedKeys(conn.getMetaData());
			BeanUtils.copyProperties(table, getTableConfigMap(table.getSqlName()));
			return table;
		}catch(SQLException e) {
			throw new RuntimeException("create table object error,tableName:"+realTableName,e);
		}
	}
	
	private List getAllTables(Connection conn) throws SQLException {
		DatabaseMetaData dbMetaData = conn.getMetaData();
		ResultSet rs = dbMetaData.getTables(getCatalog(), getSchema(), null, null);
		List tables = new ArrayList();
		while(rs.next()) {
			Table table = createTable(conn, rs);
			tables.add(table);
		}
		return tables;
	}

	private String getSynonymOwner(String synonymName)  {
	      PreparedStatement ps = null;
	      ResultSet rs = null;
	      String ret = null;
	      try {
	         ps = getConnection().prepareStatement("select table_owner from sys.all_synonyms where table_name=? and owner=?");
	         ps.setString(1, synonymName);
	         ps.setString(2, getSchema());
	         rs = ps.executeQuery();
	         if (rs.next()) {
	            ret = rs.getString(1);
	         }
	         else {
	            String databaseStructure = getDatabaseStructureInfo();
	            throw new RuntimeException("Wow! Synonym " + synonymName + " not found. How can it happen? " + databaseStructure);
	         }
	      } catch (SQLException e) {
	         String databaseStructure = getDatabaseStructureInfo();
	         GLogger.error(e.getMessage(), e);
	         throw new RuntimeException("Exception in getting synonym owner " + databaseStructure);
	      } finally {
	         dbHelper.close(rs,ps);
	      }
	      return ret;
	   }
   
   private String getDatabaseStructureInfo() {
	      ResultSet schemaRs = null;
	      ResultSet catalogRs = null;
	      String nl = System.getProperty("line.separator");
	      StringBuffer sb = new StringBuffer(nl);
	      // Let's give the user some feedback. The exception
	      // is probably related to incorrect schema configuration.
	      sb.append("Configured schema:").append(getSchema()).append(nl);
	      sb.append("Configured catalog:").append(getCatalog()).append(nl);

	      try {
	         schemaRs = getMetaData().getSchemas();
	         sb.append("Available schemas:").append(nl);
	         while (schemaRs.next()) {
	            sb.append("  ").append(schemaRs.getString("TABLE_SCHEM")).append(nl);
	         }
	      } catch (SQLException e2) {
	         GLogger.warn("Couldn't get schemas", e2);
	         sb.append("  ?? Couldn't get schemas ??").append(nl);
	      } finally {
	         dbHelper.close(schemaRs,null);
	      }

	      try {
	         catalogRs = getMetaData().getCatalogs();
	         sb.append("Available catalogs:").append(nl);
	         while (catalogRs.next()) {
	            sb.append("  ").append(catalogRs.getString("TABLE_CAT")).append(nl);
	         }
	      } catch (SQLException e2) {
	         GLogger.warn("Couldn't get catalogs", e2);
	         sb.append("  ?? Couldn't get catalogs ??").append(nl);
	      } finally {
	         dbHelper.close(catalogRs,null);
	      }
	      return sb.toString();
    }
	   
	private DatabaseMetaData getMetaData() throws SQLException {
		return getConnection().getMetaData();
	}
	
	private void retriveTableColumns(Table table) throws SQLException {
	      GLogger.debug("-------setColumns(" + table.getSqlName() + ")");

	      List primaryKeys = getTablePrimaryKeys(table);
	      table.setPrimaryKeyColumns(primaryKeys);
	      
	      // get the indices and unique columns
	      List indices = new LinkedList();
	      // maps index names to a list of columns in the index
	      Map uniqueIndices = new HashMap();
	      // maps column names to the index name.
	      Map uniqueColumns = new HashMap();
	      ResultSet indexRs = null;

	      try {

	         if (table.getOwnerSynonymName() != null) {
	            indexRs = getMetaData().getIndexInfo(getCatalog(), table.getOwnerSynonymName(), table.getSqlName(), false, true);
	         }
	         else {
	            indexRs = getMetaData().getIndexInfo(getCatalog(), getSchema(), table.getSqlName(), false, true);
	         }
	         while (indexRs.next()) {
	            String columnName = indexRs.getString("COLUMN_NAME");
	            if (columnName != null) {
	               GLogger.debug("index:" + columnName);
	               indices.add(columnName);
	            }

	            // now look for unique columns
	            String indexName = indexRs.getString("INDEX_NAME");
	            boolean nonUnique = indexRs.getBoolean("NON_UNIQUE");

	            if (!nonUnique && columnName != null && indexName != null) {
	               List l = (List)uniqueColumns.get(indexName);
	               if (l == null) {
	                  l = new ArrayList();
	                  uniqueColumns.put(indexName, l);
	               }
	               l.add(columnName);
	               uniqueIndices.put(columnName, indexName);
	               GLogger.debug("unique:" + columnName + " (" + indexName + ")");
	            }
	         }
	      } catch (Throwable t) {
	         // Bug #604761 Oracle getIndexInfo() needs major grants
	         // http://sourceforge.net/tracker/index.php?func=detail&aid=604761&group_id=36044&atid=415990
	      } finally {
	         dbHelper.close(indexRs,null);
	      }

	      List columns = getTableColumns(table, primaryKeys, indices, uniqueIndices, uniqueColumns);

	      for (Iterator i = columns.iterator(); i.hasNext(); ) {
	         Column column = (Column)i.next();
	         table.addColumn(column);
	      }

	      // In case none of the columns were primary keys, issue a warning.
	      if (primaryKeys.size() == 0) {
	         GLogger.warn("WARNING: The JDBC driver didn't report any primary key columns in " + table.getSqlName());
	      }
	}

	private List getTableColumns(Table table, List primaryKeys, List indices, Map uniqueIndices, Map uniqueColumns) throws SQLException {
		// get the columns
	      List columns = new LinkedList();
	      ResultSet columnRs = getColumnsResultSet(table);
	      
	      while (columnRs.next()) {
	         int sqlType = columnRs.getInt("DATA_TYPE");
	         String sqlTypeName = columnRs.getString("TYPE_NAME");
	         String columnName = columnRs.getString("COLUMN_NAME");
	         String columnDefaultValue = columnRs.getString("COLUMN_DEF");
	         
	         String remarks = columnRs.getString("REMARKS");
	         if(remarks == null && dbHelper.isOracleDataBase()) {
	        	 remarks = getOracleColumnComments(table.getSqlName(), columnName);
	         }
	         
	         // if columnNoNulls or columnNullableUnknown assume "not nullable"
	         boolean isNullable = (DatabaseMetaData.columnNullable == columnRs.getInt("NULLABLE"));
	         int size = columnRs.getInt("COLUMN_SIZE");
	         int decimalDigits = columnRs.getInt("DECIMAL_DIGITS");

	         boolean isPk = primaryKeys.contains(columnName);
	         boolean isIndexed = indices.contains(columnName);
	         String uniqueIndex = (String)uniqueIndices.get(columnName);
	         List columnsInUniqueIndex = null;
	         if (uniqueIndex != null) {
	            columnsInUniqueIndex = (List)uniqueColumns.get(uniqueIndex);
	         }

	         boolean isUnique = columnsInUniqueIndex != null && columnsInUniqueIndex.size() == 1;
	         if (isUnique) {
	            GLogger.debug("unique column:" + columnName);
	         }
	         Column column = new Column(
	               table,
	               sqlType,
	               sqlTypeName,
	               columnName,
	               size,
	               decimalDigits,
	               isPk,
	               isNullable,
	               isIndexed,
	               isUnique,
	               columnDefaultValue,
	               remarks);
	         BeanUtils.copyProperties(column,getColumnOverrideValue(table,column));
	         columns.add(column);
	    }
	    columnRs.close();
		return columns;
	}

	private Map getColumnOverrideValue(Table table, Column column) {
		NodeData root = getTableConfigXml(table.getSqlName());
		 if(root != null){
			 for(NodeData item : root.childs) {
				 if(item.nodeName.equals(column.getSqlName())) {
					 return item.getElementMap("sqlName");
				 }
		     }
		 }
		 return new HashMap();
	}

	private ResultSet getColumnsResultSet(Table table) throws SQLException {
		ResultSet columnRs = null;
	    if (table.getOwnerSynonymName() != null) {
	         columnRs = getMetaData().getColumns(getCatalog(), table.getOwnerSynonymName(), table.getSqlName(), null);
	    } else {
	         columnRs = getMetaData().getColumns(getCatalog(), getSchema(), table.getSqlName(), null);
	    }
		return columnRs;
	}

	private List<String> getTablePrimaryKeys(Table table) throws SQLException {
		// get the primary keys
	      List primaryKeys = new LinkedList();
	      ResultSet primaryKeyRs = null;
	      if (table.getOwnerSynonymName() != null) {
	         primaryKeyRs = getMetaData().getPrimaryKeys(getCatalog(), table.getOwnerSynonymName(), table.getSqlName());
	      }
	      else {
	         primaryKeyRs = getMetaData().getPrimaryKeys(getCatalog(), getSchema(), table.getSqlName());
	      }
	      while (primaryKeyRs.next()) {
	         String columnName = primaryKeyRs.getString("COLUMN_NAME");
	         GLogger.debug("primary key:" + columnName);
	         primaryKeys.add(columnName);
	      }
	      primaryKeyRs.close();
		return primaryKeys;
	}

	private String getOracleTableComments(String table)  {
		String sql = "SELECT comments FROM user_tab_comments WHERE table_name='"+table+"'";
		return dbHelper.queryForString(sql);
	}

	private String getOracleColumnComments(String table,String column)  {
		String sql = "SELECT comments FROM user_col_comments WHERE table_name='"+table+"' AND column_name = '"+column+"'";
		return dbHelper.queryForString(sql);
	}

	
	class DbHelper {
		public void close(ResultSet rs,PreparedStatement ps,Statement... statements) {
			try {
				if(ps != null) ps.close();
				if(rs != null) rs.close();
				for(Statement s : statements) {s.close();}
			}catch(Exception e){
			}
		}
		public boolean isOracleDataBase() {
			boolean ret = false;
			try {
				ret = (getMetaData().getDatabaseProductName().toLowerCase()
						.indexOf("oracle") != -1);
			} catch (Exception ignore) {
			}
			return ret;
		}
		
		public String queryForString(String sql) {
			Statement s = null;
			ResultSet rs = null;
			try {
				s =  getConnection().createStatement();
				rs = s.executeQuery(sql);
				if(rs.next()) {
					return rs.getString(1);
				}
				return null;
			}catch(SQLException e) {
				e.printStackTrace();
				return null;
			}finally {
				close(rs,null,s);
			}
		}		
	}
}
