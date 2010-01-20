package cn.org.rapid_framework.web.session.store;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import cn.org.rapid_framework.util.CalendarUtils;
/**
 * <pre>
 *	CREATE TABLE http_session (
 *	  session_id char(40) PRIMARY KEY,
 *	  session_data text,
 *	  expire_date timestamp
 *	);
 * </pre>
 * 
 * @author badqiu
 *
 */
public class JdbcSessionStore implements SessionStore{
	DataSource dataSource;
	
	static String DELETE = "delete from http_session where session_id = ?";
	static String INSERT = "insert into http_session(session_id,session_data,expire_date) values (?,?,?)";
	static String GET = "select session_data from http_session where session_id = ? and expire_date >= ?";
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void deleteSession(HttpServletResponse response,String sessionId) {
		getSimpleJdbcTemplate().update(DELETE, sessionId);
	}

	public Map getSession(HttpServletRequest request, String sessionId,int timeoutMinute) {
		List<Map> results = getSimpleJdbcTemplate().query(GET,new ParameterizedRowMapper<Map>(){
			public Map mapRow(ResultSet rs, int row) throws SQLException {
				String sessionData = rs.getString(1);
				return decode(sessionData);
			}
		},sessionId,computeExpireDate(timeoutMinute));
		
		return results.size() > 0 ? results.get(0) : new HashMap();
	}

	public void saveSession(HttpServletResponse response, String sessionId,Map sessionData,int timeoutMinute) {
		deleteSession(response, sessionId);
		String data = encode(sessionData);
		getSimpleJdbcTemplate().update(INSERT, sessionId,data,new Timestamp(System.currentTimeMillis()));
	}

	private Timestamp computeExpireDate(int timeoutMinute) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		CalendarUtils.add(Calendar.MINUTE, now, -timeoutMinute * 60 * 1000);
		return now;
	}

	private SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return new SimpleJdbcTemplate(dataSource);
	}
	
	public static Map decode(String sessionData) {
		return SessionDataUtils.decode(sessionData);
	}
	
	public static String encode(Map sessionData) {
		return SessionDataUtils.encode(sessionData);
	}
}
