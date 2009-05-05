package cn.org.rapid_framework.jdbc.dialect;
/**
 * @author badqiu
 */
public class OracleDialect extends Oracle9Dialect{
	
	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.trim();
		boolean isForUpdate = false;
		if ( sql.toLowerCase().endsWith(" for update") ) {
			sql = sql.substring( 0, sql.length()-11 );
			isForUpdate = true;
		}
		
		StringBuffer pagingSelect = new StringBuffer( sql.length()+100 );
		if (offset > 0) {
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		}
		else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		if (offset > 0) {
			int end = offset+limit;
			pagingSelect.append(" ) row_ ) where rownum_ <= "+end+" and rownum_ > "+offset);
		}
		else {
			pagingSelect.append(" ) where rownum <= " + limit);
		}

		if ( isForUpdate ) {
			pagingSelect.append( " for update" );
		}
		
		return pagingSelect.toString();		
	}

}
