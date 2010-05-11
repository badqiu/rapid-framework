package cn.org.rapid_framework.generator.provider.db.model;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.util.StringHelper;
/**
 * 
 * @author badqiu
 * @email badqiu(a)gmail.com
 */
public class Table {

	String sqlName;
	String remarks;
	String customClassName;
	/** the name of the owner of the synonym if this table is a synonym */
	private String ownerSynonymName = null;
	Set<Column> columns = new LinkedHashSet();
	List<Column> primaryKeyColumns = new ArrayList();

	public Set<Column> getColumns() {
		return columns;
	}
	public void setColumns(Set columns) {
		this.columns = columns;
	}
	public String getOwnerSynonymName() {
		return ownerSynonymName;
	}
	public void setOwnerSynonymName(String ownerSynonymName) {
		this.ownerSynonymName = ownerSynonymName;
	}
	public List<Column> getPrimaryKeyColumns() {
		return primaryKeyColumns;
	}
	public void setPrimaryKeyColumns(List<Column> primaryKeyColumns) {
		this.primaryKeyColumns = primaryKeyColumns;
	}
	public String getSqlName() {
		return sqlName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public void addColumn(Column column) {
		columns.add(column);
	}
	
	public void setClassName(String customClassName) {
		this.customClassName = customClassName;
	}
	public String getClassName() {
		String defaultValue = StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(getSqlName()));
		return StringHelper.emptyIf(customClassName, defaultValue);
	}
	public String getTableAlias() {
		return  StringHelper.emptyIf(getRemarks(), getClassName());
	}
	
	/**
	 * 等价于getClassName().toLowerCase()
	 * @return
	 */
	public String getClassNameLowerCase() {
		return getClassName().toLowerCase();
	}
	/**
	 * 等价于getSqlName().toLowerCase()
	 * @return
	 */
	public String getUnderscoreName() {
		return getSqlName().toLowerCase();
	}
	/**
	 * 返回值为getClassName()的第一个字母小写
	 * @return
	 */
	public String getClassNameFirstLower() {
		return StringHelper.uncapitalize(getClassName());
	}
	
	/**
	 * 根据getClassName()计算而来,用于得到常量名,如 userInfo转换为USER_INFO
	 * @return
	 */
	public String getConstantName() {
		return StringHelper.toUnderscoreName(getClassName()).toUpperCase();
	}
	
	public boolean isSingleId() {
		return getPkCount() == 1 ? true : false;
	}
	
	public boolean isCompositeId() {
		return getPkCount() > 1 ? true : false;
	}

	public boolean isNotCompositeId() {
		return !isCompositeId();
	}
	
	/**
	 * 得到主键总数
	 * @return
	 */
	public int getPkCount() {
		int pkCount = 0;
		for(Column c : columns){
			if(c.isPk()) {
				pkCount ++;
			}
		}
		return pkCount;
	}
	/**
	 * use getPkColumns()
	 * @deprecated 
	 */
	public List getCompositeIdColumns() {
		return getPkColumns();
	}
	
	/**
	 * 得到是主键的全部column
	 * @return
	 */	
	public List getPkColumns() {
		List results = new ArrayList();
		for(Column c : getColumns()) {
			if(c.isPk())
				results.add(c);
		}
		return results;
	}
	
	/**
	 * 得到不是主键的全部column
	 * @return
	 */
	public List getNotPkColumns() {
		List results = new ArrayList();
		for(Column c : getColumns()) {
			if(!c.isPk())
				results.add(c);
		}
		return results;
	}
	
	public Column getIdColumn() {
		for(Column c : getColumns()) {
			if(c.isPk())
				return c;
		}
		return null;
	}
	
	/**
	 * 忽略过滤掉某些关键字的列,关键字不区分大小写,以逗号分隔
	 * @param ignoreKeywords
	 * @return
	 */
	public List<Column> getIgnoreKeywordsColumns(String ignoreKeywords) {
		List results = new ArrayList();
		for(Column c : getColumns()) {
			String sqlname = c.getSqlName().toLowerCase();
			if(StringHelper.contains(sqlname,ignoreKeywords.split(","))) {
				continue;
			}
			results.add(c);
		}
		return results;
	}
	
	/**
	 * This method was created in VisualAge.
	 */
	public void initImportedKeys(DatabaseMetaData dbmd) throws java.sql.SQLException {
		
			   // get imported keys a
	
			   ResultSet fkeys = dbmd.getImportedKeys(catalog,schema,this.sqlName);

			   while ( fkeys.next()) {
				 String pktable = fkeys.getString(PKTABLE_NAME);
				 String pkcol   = fkeys.getString(PKCOLUMN_NAME);
				 String fktable = fkeys.getString(FKTABLE_NAME);
				 String fkcol   = fkeys.getString(FKCOLUMN_NAME);
				 String seq     = fkeys.getString(KEY_SEQ);
				 Integer iseq   = new Integer(seq);
				 getImportedKeys().addForeignKey(pktable,pkcol,fkcol,iseq);
			   }
			   fkeys.close();
	}
	
	/**
	 * This method was created in VisualAge.
	 */
	public void initExportedKeys(DatabaseMetaData dbmd) throws java.sql.SQLException {
			   // get Exported keys
	
			   ResultSet fkeys = dbmd.getExportedKeys(catalog,schema,this.sqlName);
			  
			   while ( fkeys.next()) {
				 String pktable = fkeys.getString(PKTABLE_NAME);
				 String pkcol   = fkeys.getString(PKCOLUMN_NAME);
				 String fktable = fkeys.getString(FKTABLE_NAME);
				 String fkcol   = fkeys.getString(FKCOLUMN_NAME);
				 String seq     = fkeys.getString(KEY_SEQ);
				 Integer iseq   = new Integer(seq);
				 getExportedKeys().addForeignKey(fktable,fkcol,pkcol,iseq);
			   }
			   fkeys.close();
	}
	
	/**
	 * @return Returns the exportedKeys.
	 */
	public ForeignKeys getExportedKeys() {
		if (exportedKeys == null) {
			exportedKeys = new ForeignKeys(this);
		}
		return exportedKeys;
	}
	/**
	 * @return Returns the importedKeys.
	 */
	public ForeignKeys getImportedKeys() {
		if (importedKeys == null) {
			importedKeys = new ForeignKeys(this);
		}
		return importedKeys;
	}
	
	public String toString() {
		return "Database Table:"+getSqlName()+" to ClassName:"+getClassName();
	}
	
	String catalog = DbTableFactory.getInstance().getCatalog();
	String schema = DbTableFactory.getInstance().getSchema();
	
	private ForeignKeys exportedKeys;
	private ForeignKeys importedKeys;
	
	public    static final String PKTABLE_NAME  = "PKTABLE_NAME";
	public    static final String PKCOLUMN_NAME = "PKCOLUMN_NAME";
	public    static final String FKTABLE_NAME  = "FKTABLE_NAME";
	public    static final String FKCOLUMN_NAME = "FKCOLUMN_NAME";
	public    static final String KEY_SEQ       = "KEY_SEQ";
}
