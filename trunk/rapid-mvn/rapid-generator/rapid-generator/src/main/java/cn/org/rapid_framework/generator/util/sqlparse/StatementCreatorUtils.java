package cn.org.rapid_framework.generator.util.sqlparse;

import java.sql.Types;
import java.util.Date;
import java.util.Random;


public class StatementCreatorUtils {

    public static Object getRandomValue(int sqlType,Integer scale)  {
        switch(sqlType) {
            case Types.BIT:;
            case Types.TINYINT:;
            case Types.SMALLINT:;
            case Types.INTEGER:;
            case Types.BIGINT:;
            case Types.FLOAT:;
            case Types.REAL:;
            case Types.DOUBLE:;
            case Types.NUMERIC:;
            case Types.DECIMAL:;
                return randomNumber();
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                return now();
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.LONGVARCHAR:
            case Types.CLOB:
                return randomString();
            case Types.NVARCHAR:
            case Types.NCHAR:
            case Types.NCLOB:
                return randomString();
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                return null;
            case Types.NULL:
            case Types.OTHER:
            case Types.BLOB:
                return null;
            case Types.BOOLEAN:
                return false;
            case -10: //CURSOR
            case Integer.MIN_VALUE + 1000: //UNDEFINED
                return null;                
            default:
                return "";
        }
    }
    
    public static byte randomNumber() {
        return (byte)new Random(System.currentTimeMillis()).nextInt();
    }
    
    public static Date now() {
        return new Date();
    }
    
    public static String randomString() {
        char c = (char)(65 + (randomNumber() % 23));
        return new String(new char[]{c});
    }
    
    
    
//    private static void setParameterValueInternal(PreparedStatement ps, int paramIndex,
//                                                  int sqlType, Integer scale,
//                                                  Object inValue) throws SQLException {
//
//        int sqlTypeToUse = sqlType;
//        Object inValueToUse = inValue;
//
//        GLogger.debug("Setting SQL statement parameter value: column index "
//                     + paramIndex
//                     + ", parameter value ["
//                     + inValueToUse
//                     + "], value class ["
//                     + (inValueToUse != null ? inValueToUse.getClass().getName() : "null")
//                     + "], SQL type "
//                     + (sqlTypeToUse == SqlTypeValue.TYPE_UNKNOWN ? "unknown" : Integer
//                         .toString(sqlTypeToUse)));
//
//        if (inValueToUse == null) {
//            if (sqlTypeToUse == SqlTypeValue.TYPE_UNKNOWN) {
//                boolean useSetObject = false;
//                sqlTypeToUse = Types.NULL;
//                try {
//                    DatabaseMetaData dbmd = ps.getConnection().getMetaData();
//                    String databaseProductName = dbmd.getDatabaseProductName();
//                    String jdbcDriverName = dbmd.getDriverName();
//                    if (databaseProductName.startsWith("Informix")
//                        || jdbcDriverName.startsWith("Microsoft SQL Server")
//                        || jdbcDriverName.startsWith("Apache Derby Embedded")) {
//                        useSetObject = true;
//                    } else if (databaseProductName.startsWith("DB2")
//                               || jdbcDriverName.startsWith("jConnect")
//                               || jdbcDriverName.startsWith("SQLServer")) {
//                        sqlTypeToUse = Types.VARCHAR;
//                    }
//                } catch (Throwable ex) {
//                    GLogger.debug("Could not check database or driver name,cause:"+ ex);
//                }
//                if (useSetObject) {
//                    ps.setObject(paramIndex, null);
//                } else {
//                    ps.setNull(paramIndex, sqlTypeToUse);
//                }
//            } else {
//                ps.setNull(paramIndex, sqlTypeToUse);
//            }
//        }
//
//        else { // inValue != null
//            if (sqlTypeToUse == Types.VARCHAR || sqlTypeToUse == Types.LONGVARCHAR
//                       || (sqlTypeToUse == Types.CLOB && isStringValue(inValueToUse.getClass()))) {
//                ps.setString(paramIndex, inValueToUse.toString());
//            } else if (sqlTypeToUse == Types.DECIMAL || sqlTypeToUse == Types.NUMERIC) {
//                if (inValueToUse instanceof BigDecimal) {
//                    ps.setBigDecimal(paramIndex, (BigDecimal) inValueToUse);
//                } else if (scale != null) {
//                    ps.setObject(paramIndex, inValueToUse, sqlTypeToUse, scale.intValue());
//                } else {
//                    ps.setObject(paramIndex, inValueToUse, sqlTypeToUse);
//                }
//            } else if (sqlTypeToUse == Types.DATE) {
//                if (inValueToUse instanceof java.util.Date) {
//                    if (inValueToUse instanceof java.sql.Date) {
//                        ps.setDate(paramIndex, (java.sql.Date) inValueToUse);
//                    } else {
//                        ps.setDate(paramIndex, new java.sql.Date(((java.util.Date) inValueToUse)
//                            .getTime()));
//                    }
//                } else if (inValueToUse instanceof Calendar) {
//                    Calendar cal = (Calendar) inValueToUse;
//                    ps.setDate(paramIndex, new java.sql.Date(cal.getTime().getTime()), cal);
//                } else {
//                    ps.setObject(paramIndex, inValueToUse, Types.DATE);
//                }
//            } else if (sqlTypeToUse == Types.TIME) {
//                if (inValueToUse instanceof java.util.Date) {
//                    if (inValueToUse instanceof java.sql.Time) {
//                        ps.setTime(paramIndex, (java.sql.Time) inValueToUse);
//                    } else {
//                        ps.setTime(paramIndex, new java.sql.Time(((java.util.Date) inValueToUse)
//                            .getTime()));
//                    }
//                } else if (inValueToUse instanceof Calendar) {
//                    Calendar cal = (Calendar) inValueToUse;
//                    ps.setTime(paramIndex, new java.sql.Time(cal.getTime().getTime()), cal);
//                } else {
//                    ps.setObject(paramIndex, inValueToUse, Types.TIME);
//                }
//            } else if (sqlTypeToUse == Types.TIMESTAMP) {
//                if (inValueToUse instanceof java.util.Date) {
//                    if (inValueToUse instanceof java.sql.Timestamp) {
//                        ps.setTimestamp(paramIndex, (java.sql.Timestamp) inValueToUse);
//                    } else {
//                        ps.setTimestamp(paramIndex, new java.sql.Timestamp(
//                            ((java.util.Date) inValueToUse).getTime()));
//                    }
//                } else if (inValueToUse instanceof Calendar) {
//                    Calendar cal = (Calendar) inValueToUse;
//                    ps.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()),
//                        cal);
//                } else {
//                    ps.setObject(paramIndex, inValueToUse, Types.TIMESTAMP);
//                }
//            } else if (sqlTypeToUse == SqlTypeValue.TYPE_UNKNOWN) {
//                if (isStringValue(inValueToUse.getClass())) {
//                    ps.setString(paramIndex, inValueToUse.toString());
//                } else if (isDateValue(inValueToUse.getClass())) {
//                    ps.setTimestamp(paramIndex, new java.sql.Timestamp(
//                        ((java.util.Date) inValueToUse).getTime()));
//                } else if (inValueToUse instanceof Calendar) {
//                    Calendar cal = (Calendar) inValueToUse;
//                    ps.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()));
//                } else {
//                    // Fall back to generic setObject call without SQL type
//                    // specified.
//                    ps.setObject(paramIndex, inValueToUse);
//                }
//            } else {
//                // Fall back to generic setObject call with SQL type specified.
//                ps.setObject(paramIndex, inValueToUse, sqlTypeToUse);
//            }
//        }
//    }
//
//    /**
//     * Check whether the given value can be treated as a String value.
//     */
//    private static boolean isStringValue(Class inValueType) {
//        // Consider any CharSequence (including JDK 1.5's StringBuilder) as String.
//        return (CharSequence.class.isAssignableFrom(inValueType) ||
//                StringWriter.class.isAssignableFrom(inValueType));
//    }
//
//    /**
//     * Check whether the given value is a <code>java.util.Date</code>
//     * (but not one of the JDBC-specific subclasses).
//     */
//    private static boolean isDateValue(Class inValueType) {
//        return (java.util.Date.class.isAssignableFrom(inValueType) &&
//                !(java.sql.Date.class.isAssignableFrom(inValueType) ||
//                        java.sql.Time.class.isAssignableFrom(inValueType) ||
//                        java.sql.Timestamp.class.isAssignableFrom(inValueType)));
//    }
}
