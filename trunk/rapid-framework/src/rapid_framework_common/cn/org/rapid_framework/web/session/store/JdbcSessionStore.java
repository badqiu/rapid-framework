package cn.org.rapid_framework.web.session.store;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
/**
 * <pre>
 *	CREATE TABLE http_session_store (
 *	  session_id char(40) PRIMARY KEY,
 *	  session_data text,
 *	  expire_date bigint
 *	);
 * </pre>
 *
 * @author badqiu
 *
 */
public class JdbcSessionStore extends SessionStore{
	DataSource dataSource;

	static String DELETE = "delete from http_session_store where session_id = ?";
	static String INSERT = "insert into http_session_store(session_id,expire_date,session_data) values (?,?,?)";
	static String GET = "select session_data from http_session_store where session_id = ? and expire_date >= ?";

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void deleteSession(String sessionId) {
		getSimpleJdbcTemplate().update(DELETE, sessionId);
	}

	public Map getSession(String sessionId,int timeoutSeconds) {
		List<Map> results = getSimpleJdbcTemplate().query(GET,new ParameterizedRowMapper<Map>(){
			public Map mapRow(ResultSet rs, int row) throws SQLException {
				String sessionData = rs.getString(1);
				return decode(sessionData);
			}
		},sessionId,System.currentTimeMillis());
		
		return results.size() > 0 ? results.get(0) : new HashMap();
	}

	public void saveSession(String sessionId,Map sessionData,int timeoutSeconds) {
		deleteSession(sessionId);
		String data = encode(sessionData);
		Timestamp expire_date = computeExpireDate(timeoutSeconds);
		getSimpleJdbcTemplate().update(INSERT, sessionId,expire_date.getTime(),data);
	}

	private Timestamp computeExpireDate(int timeoutSeconds) {
//		Timestamp now = new Timestamp(System.currentTimeMillis());
//		CalendarUtils.add(Calendar.SECOND, now, timeoutSeconds);
//		return now;
		return new Timestamp(System.currentTimeMillis()+ (timeoutSeconds * 1000));
	}

	private SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return new SimpleJdbcTemplate(dataSource);
	}

	static Pattern sessionDataParser = Pattern.compile("\u0000([^:]*):([^\u0000]*)\u0000");
	public static Map decode(String sessionData) {
		if(sessionData == null) return new HashMap();
		
		Map map = new HashMap();
		Matcher matcher = sessionDataParser.matcher(sessionData);
		while (matcher.find()) {
			map.put(matcher.group(1), matcher.group(2));
		}
		return map;
	}

	public static String encode(Map<String,?> sessionData) {
		StringBuilder encodeString = new StringBuilder();
		for (String key : sessionData.keySet()) {
			encodeString.append("\u0000");
			encodeString.append(key);
			encodeString.append(":");
			encodeString.append(sessionData.get(key));
			encodeString.append("\u0000");
		}
		return encodeString.toString();
	}
	
}
