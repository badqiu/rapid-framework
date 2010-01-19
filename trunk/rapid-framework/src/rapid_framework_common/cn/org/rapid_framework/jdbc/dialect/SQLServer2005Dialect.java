package cn.org.rapid_framework.jdbc.dialect;

/**
 *
 * @author badqiu
 *
 */
// Hibernate BUG: http://opensource.atlassian.com/projects/hibernate/browse/HHH-2655
public class SQLServer2005Dialect extends Dialect{

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public boolean supportsLimitOffset() {
		return true;
	}

	/**
	 * Add a LIMIT clause to the given SQL SELECT
	 *
	 * The LIMIT SQL will look like:
	 *
	 * WITH query AS
	 * (SELECT TOP 100 percent ROW_NUMBER() OVER (ORDER BY orderby) as __hibernate_row_nr__, ... original_query)
	 * SELECT *
	 * FROM query
	 * WHERE __hibernate_row_nr__ > offset
	 * ORDER BY __hibernate_row_nr__
	 *
	 * @param querySqlString The SQL statement to base the limit query off of.
	 * @param offset         Offset of the first row to be returned by the query (zero-based)
	 * @param last           Maximum number of rows to be returned by the query
	 * @return A new SQL statement with the LIMIT clause applied.
	 */
	@Override
	public String getLimitString(String querySqlString, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
		/*
		 * WITH query AS
		 *     (SELECT TOP 100 percent ROW_NUMBER() OVER (ORDER BY orderby) as __hibernate_row_nr__, ... original_query)
		 * SELECT *
		 * FROM query
		 * WHERE __hibernate_row_nr__ > offset
		 * ORDER BY __hibernate_row_nr__
		 */
		StringBuffer pagingBuilder = new StringBuffer();
		String orderby = getOrderByPart(querySqlString);
		String distinctStr = "";

		String loweredString = querySqlString.toLowerCase();
		String sqlPartString = querySqlString;
		if (loweredString.trim().startsWith("select")) {
			int index = 6;
			if (loweredString.startsWith("select distinct")) {
				distinctStr = "DISTINCT ";
				index = 15;
			}
			sqlPartString = sqlPartString.substring(index);
		}
		pagingBuilder.append(sqlPartString);

		// if no ORDER BY is specified use fake ORDER BY field to avoid errors
		if (orderby == null || orderby.length() == 0) {
			orderby = "ORDER BY CURRENT_TIMESTAMP";
		}

		StringBuffer result = new StringBuffer();
		result.append("WITH query AS (SELECT ")
				.append(distinctStr)
				.append("TOP 100 PERCENT ")
				.append(" ROW_NUMBER() OVER (")
				.append(orderby)
				.append(") as __hibernate_row_nr__, ")
				.append(pagingBuilder)
				.append(") SELECT * FROM query WHERE __hibernate_row_nr__ > ")
				.append(offsetPlaceholder)
				.append(" ORDER BY __hibernate_row_nr__");

		return result.toString();
	}

	static String getOrderByPart(String sql) {
		String loweredString = sql.toLowerCase();
		int orderByIndex = loweredString.indexOf("order by");
		if (orderByIndex != -1) {
			// if we find a new "order by" then we need to ignore
			// the previous one since it was probably used for a subquery
			return sql.substring(orderByIndex);
		} else {
			return "";
		}
	}
}
