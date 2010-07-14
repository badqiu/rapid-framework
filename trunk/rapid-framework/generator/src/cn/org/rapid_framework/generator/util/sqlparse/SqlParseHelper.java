package cn.org.rapid_framework.generator.util.sqlparse;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

//	static Pattern p = Pattern.compile("(:)(\\w+)(\\|?)([\\w.]+)");
	public static String getParameterClassName(String sql, String paramName) {
		Pattern p = Pattern.compile("(:)(" + paramName + ")(\\|?)([\\w.]+)");
		Matcher m = p.matcher(sql);
		if (m.find()) {
			return m.group(4);
		}
		return null;
	}
	
	public static String getColumnNameByRightCondition(String sql,String column) {
		String operator = "[=<>!]{1,2}";
		String result = getColumnNameByRightCondition(sql, column, operator);
		if(result == null) {
			result = getColumnNameByRightCondition(sql, column, "\\s+like\\s+");
		}
		if(result == null) {
			result = getColumnNameByRightCondition(sql, column, "\\s+between\\s+");
		}
		if(result == null) {
			result = getColumnNameByRightCondition(sql, column, "\\s+between\\s.+\\sand\\s+");
		}
		if(result == null) {
			result = getColumnNameByRightCondition(sql, column, "\\s+in\\s+\\(");
		}
		return result;
	}

	private static String getColumnNameByRightCondition(String sql,
			String column, String operator) {
		Pattern p = Pattern.compile("(\\w+)\\s*"+operator+"\\s*[:#$&]\\{?"+column+"[\\}#$]?",Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		if(m.find()) {
			return m.group(1);
		}
		return null;
	}

	public static String convert2ParametersString(String sql,String prefix,String suffix) {
	    return new NamedSqlConverter(prefix,suffix).convert2ParametersString(sql);
	}

	 /**
     * 将sql从占位符转换为命名参数,如 select * from user where id =? ,将返回: select * from user where id = #id#
     * @param sql
     * @param prefix 命名参数的前缀
     * @param suffix 命名参数的后缀
     * @return
     */
	public static class NamedSqlConverter {
	    private String prefix;
	    private String suffix;
	    
        public NamedSqlConverter(String prefix, String suffix) {
            if(prefix == null) throw new NullPointerException("'prefix' must be not null");
            if(suffix == null) throw new NullPointerException("'suffix' must be not null");
            this.prefix = prefix;
            this.suffix = suffix;
        }

        public String convert2ParametersString(String sql) {
	        if(sql.trim().toLowerCase().matches("(?is)\\s*insert\\s+into\\s+.*")) {
	            return replace2NamedParameters(replaceInsertSql2NamedParameters(sql));
	        }else {
	            return replace2NamedParameters(sql);
	        }
	    }

        private String replace2NamedParameters(String sql) {
            String replacedSql = replace2NamedParametersByOperator(sql,"[=<>!]{1,2}");
            return replace2NamedParametersByOperator(replacedSql,"\\s+like\\s+");
        }

        private String replaceInsertSql2NamedParameters(String sql) {
            Pattern p = Pattern.compile("\\s*insert\\s+into.*\\((.*?)\\).*values.*?\\((.*)\\).*",Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(sql);
            StringBuffer sb = new StringBuffer();
            if(m.find()) {
                String[] columns = StringHelper.tokenizeToStringArray(m.group(1),", \t\n\r\f");
                String[] values = StringHelper.tokenizeToStringArray(m.group(2),", \t\n\r\f");
                if(columns.length != values.length) {
                    throw new IllegalArgumentException("insert 语句的插入列与值列数目不相等,sql:"+sql+" columns:"+StringHelper.join(columns,",")+" values:"+StringHelper.join(values,","));
                }
                for(int i = 0; i < columns.length; i++) {
                    String column = columns[i];
                    String paranName = StringHelper.uncapitalize(StringHelper.makeAllWordFirstLetterUpperCase(column));
                    values[i] = values[i].replace("?", prefix + paranName + suffix);;
                }
                m.appendReplacement(sb, "insert into ("+StringHelper.join(columns,",")+") values ("+StringHelper.join(values,",")+")");
                
            }
            m.appendTail(sb);
            return sb.toString();
        }
	    
        private String replace2NamedParametersByOperator(String sql,String operator) {
            Pattern p = Pattern.compile("(\\w+)\\s*"+operator+"\\s*\\?",Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(sql);
            StringBuffer sb = new StringBuffer();
            while(m.find()) {
                String segment = m.group(0);
                String columnSqlName = m.group(1);
                String paramName = StringHelper.uncapitalize(StringHelper.makeAllWordFirstLetterUpperCase(columnSqlName));
                String replacedSegment = segment.replace("?", prefix + paramName + suffix);
                m.appendReplacement(sb, replacedSegment);
            }
            m.appendTail(sb);
            return sb.toString();
        }
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
		if(StringHelper.isBlank(sql)) throw new IllegalArgumentException("sql must be not empty");
		int beginPos = sql.toLowerCase().indexOf("from");
		if(beginPos == -1) throw new IllegalArgumentException(" sql : " + sql + " must has a keyword 'from'");
		return sql.substring(beginPos);
	}

	public static String getSelect(String sql) {
		if(StringHelper.isBlank(sql)) throw new IllegalArgumentException("sql must be not empty");
		int beginPos = sql.toLowerCase().indexOf("from");
		if(beginPos == -1) throw new IllegalArgumentException(" sql : " + sql + " must has a keyword 'from'");
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
										} catch (SQLException e32) {
										    try {
										        ps.setNull(i, java.sql.Types.OTHER);
										    }catch(SQLException error){
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
	}

	private static void warn(String sql, int i, SQLException error) {
		GLogger.warn("error on set parametet index:" + i + " cause:" + error
				+ " sql:" + sql);
	}
}
