package cn.org.rapid_framework.generator.util.sqlparse;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.StringHelper;

public class SqlParseHelper {
	static Pattern from = Pattern.compile("(from\\s+)([,\\w]+)",
			Pattern.CASE_INSENSITIVE);
	static Pattern join = Pattern.compile("(join\\s+)(\\w+)",
			Pattern.CASE_INSENSITIVE);
	static Pattern update = Pattern.compile("(\\s*update\\s+)(\\w+)",
			Pattern.CASE_INSENSITIVE);
	static Pattern insert = Pattern.compile("(\\s*insert\\s+into\\s+)(\\w+)",
			Pattern.CASE_INSENSITIVE);

	public static Set<String> getTableNamesByQuery(String sql) {
		sql = sql.trim();
		Set<String> result = new LinkedHashSet();
		Matcher m = from.matcher(sql);
		if (m.find()) {
			result.addAll(Arrays.asList(StringHelper.tokenizeToStringArray(m
					.group(2), ",")));
		}

		m = join.matcher(sql);
		if (m.find()) {
			result.add(m.group(2));
		}

		m = update.matcher(sql);
		if (m.find()) {
			result.add(m.group(2));
		}

		m = insert.matcher(sql);
		if (m.find()) {
			result.add(m.group(2));
		}
		return result;
	}

	static Pattern p = Pattern.compile("(:)(\\w+)(\\|?)([\\w.]+)");

	public static String getParameterClassName(String sql, String paramName) {
		Pattern p = Pattern.compile("(:)(" + paramName + ")(\\|?)([\\w.]+)");
		Matcher m = p.matcher(sql);
		if (m.find()) {
			return m.group(4);
		}
		return null;
	}

	/**
	 * 美化sql
	 * 
	 * @param sql
	 * @return
	 */
	public static String getPrettySql(String sql) {
		try {
			if (IOHelper.readLines(new StringReader(sql)).size() > 1) {
				return sql;
			} else {
				return StringHelper.replace(StringHelper.replace(sql, "from",
						"\n\tfrom"), "where", "\n\twhere");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 去除select 子句，未考虑union的情况
	 * 
	 * @param sql
	 * @return
	 */
	public static String removeSelect(String sql) {
		Assert.hasText(sql);
		int beginPos = sql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " sql : " + sql
				+ " must has a keyword 'from'");
		return sql.substring(beginPos);
	}

	public static String getSelect(String sql) {
		Assert.hasText(sql);
		int beginPos = sql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " sql : " + sql
				+ " must has a keyword 'from'");
		return sql.substring(0,beginPos);
	}
	
	public static long startTimes = System.currentTimeMillis();
	public static void setRandomParamsValueForPreparedStatement(String sql,
			PreparedStatement ps) throws SQLException {
		int count = StringHelper.containsCount(sql, "?");
		for (int i = 1; i <= count; i++) {
			long random = new Random(System.currentTimeMillis()+startTimes++).nextInt() * 30 + System.currentTimeMillis() + startTimes;
			try {
				ps.setLong(i, random);
			} catch (SQLException e) {
				try {
					ps.setInt(i, (int) random % Integer.MAX_VALUE);
				} catch (SQLException e1) {
					try {
						ps.setString(i, "" + random);
					} catch (SQLException e2) {
						try {
							ps.setTimestamp(i, new java.sql.Timestamp(random));
						} catch (SQLException e3) {
							try {
								ps.setDate(i, new java.sql.Date(random));
							} catch (SQLException e6) {
								try {
									ps.setString(i, "" + (int) random);
								} catch (SQLException e4) {
									try {
										ps.setString(i, "" + (short) random);
									} catch (SQLException e82) {
										try {
											ps.setString(i, "" + (byte) random);
										} catch (SQLException error) {
											warn(sql, i, error);
										}
									}
								}
							}
						}
					}
				}

			}
		}
	}

	private static void warn(String sql, int i, SQLException error) {
		GLogger.warn("error on set parametet index:" + i + " cause:" + error
				+ " sql:" + sql);
	}
}
