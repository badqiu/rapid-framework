package cn.org.rapid_framework.web.session.store;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

// #TODO fixed me, cannot use sun.misc.BASE64Decoder
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 
 * 用于将session存储在数据库中
 * 
 * 数据库表结构如下:
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
@SuppressWarnings("all")
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
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(sessionData)));
			return (Map)ois.readObject();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("decode session data error:UnsupportedEncodingException",e);
		} catch (IOException e) {
			throw new RuntimeException("decode session data error:IOException",e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("decode session data error:ClassNotFoundException",e);
		}
	}

	public static String encode(Map<String,?> sessionData) {
		try {
			ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(byteOutput);
			oos.writeObject(sessionData);
			byte[] bytes = byteOutput.toByteArray();
			return new BASE64Encoder().encode(bytes);
		}catch(IOException e) {
			throw new RuntimeException("encode session data error",e);
		}
	}
	
}
